package com.example.agricultureFederation.repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class StatisticsRepository {

    private final DataSource dataSource;

    public StatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<Integer, BigDecimal> getTotalPaidByMemberInPeriod(int collectiveId, LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT c.id_member, COALESCE(SUM(mf.amount), 0) AS total_paid
            FROM contribution c
            LEFT JOIN membershipfees mf ON mf.id_contribution = c.id_contribution
                AND mf.membershipfees_date BETWEEN ? AND ?
            JOIN member m ON m.id_member = c.id_member AND m.is_resigned = FALSE
            WHERE c.id_collective = ?
            GROUP BY c.id_member
            """;
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));
            stmt.setInt(3, collectiveId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("id_member"), rs.getBigDecimal("total_paid"));
            }
        }
        return result;
    }

    public Map<Integer, BigDecimal> getExpectedActiveContributionsByMember(int collectiveId, LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT c.id_member, COALESCE(SUM(c.amount), 0) AS total_expected
            FROM contribution c
            JOIN member m ON m.id_member = c.id_member AND m.is_resigned = FALSE
            WHERE c.id_collective = ?
              AND c.is_paid = FALSE
              AND c.is_active = TRUE
              AND (c.due_date IS NULL OR c.due_date BETWEEN ? AND ?)
            GROUP BY c.id_member
            """;
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("id_member"), rs.getBigDecimal("total_expected"));
            }
        }
        return result;
    }

    public Map<Integer, Integer> countActiveMembersPerCollective() throws SQLException {
        String sql = """
            SELECT id_collective, COUNT(*) AS active_count
            FROM member
            WHERE is_resigned = FALSE
            GROUP BY id_collective
            """;
        Map<Integer, Integer> result = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("id_collective"), rs.getInt("active_count"));
            }
        }
        return result;
    }

    public Map<Integer, Integer> countMembersUpToDatePerCollective(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT m.id_collective,
                   COUNT(DISTINCT m.id_member) AS up_to_date_count
            FROM member m
            WHERE m.is_resigned = FALSE
              AND NOT EXISTS (
                  SELECT 1 FROM contribution c
                  WHERE c.id_member = m.id_member
                    AND c.is_paid = FALSE
                    AND c.is_active = TRUE
                    AND c.due_date IS NOT NULL
                    AND c.due_date BETWEEN ? AND ?
              )
            GROUP BY m.id_collective
            """;
        Map<Integer, Integer> result = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("id_collective"), rs.getInt("up_to_date_count"));
            }
        }
        return result;
    }

    public Map<Integer, Integer> countNewMembersPerCollective(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT id_collective, COUNT(*) AS new_count
            FROM member
            WHERE join_date BETWEEN ? AND ?
            GROUP BY id_collective
            """;
        Map<Integer, Integer> result = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("id_collective"), rs.getInt("new_count"));
            }
        }
        return result;
    }

    public List<Integer> getAllCollectiveIds() throws SQLException {
        String sql = "SELECT id_collective FROM collective ORDER BY id_collective";
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) ids.add(rs.getInt("id_collective"));
        }
        return ids;
    }

    public String[] getCollectiveNameAndNumber(int collectiveId) throws SQLException {
        String sql = "SELECT name, location FROM collective WHERE id_collective = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("name"), rs.getString("location")};
            }
        }
        return new String[]{null, null};
    }

    public List<int[]> getActiveMemberIds(int collectiveId) throws SQLException {
        String sql = "SELECT id_member FROM member WHERE id_collective = ? AND is_resigned = FALSE";
        List<int[]> ids = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) ids.add(new int[]{rs.getInt("id_member")});
        }
        return ids;
    }

    public String[] getMemberName(int memberId) throws SQLException {
        String sql = "SELECT first_name, last_name FROM member WHERE id_member = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("first_name"), rs.getString("last_name")};
            }
        }
        return new String[]{null, null};
    }
}

