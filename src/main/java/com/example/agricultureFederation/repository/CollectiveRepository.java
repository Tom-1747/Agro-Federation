package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.Collective;
import javax.sql.DataSource;
import java.sql.*;

public class CollectiveRepository {

    private final DataSource dataSource;

    public CollectiveRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Collective save(Collective collective) throws SQLException {
        String sql = "INSERT INTO collective (id_federation, id_specialty, id_branch, name, location, phone, creation_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING *";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collective.getFederationId());
            stmt.setObject(2, collective.getSpecialityId());
            stmt.setObject(3, collective.getBranchId());
            stmt.setString(4, collective.getName());
            stmt.setString(5, collective.getLocation());
            stmt.setString(6, collective.getPhone());
            stmt.setDate(7, Date.valueOf(collective.getCreationDate()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                collective.setCollectiveId(rs.getInt("id_collective"));
            }
            return collective;
        }
    }

    public Collective findById(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM collective WHERE id_collective = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    public Collective findByIdWithMembers(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM collective WHERE id_collective = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    public int countActiveMembers(int collectiveId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM member WHERE id_collective = ? AND is_resigned = FALSE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        }
    }

    public int countMembersWithSixMonthsSeniority(int collectiveId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM member " +
                "WHERE id_collective = ? AND is_resigned = FALSE " +
                "AND join_date <= CURRENT_DATE - INTERVAL '6 months'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        }
    }

    public boolean hasAllSpecificPositionsFilled(int collectiveId, int civilYear) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT position_label) FROM position_mandate " +
                "WHERE id_collective = ? AND calendar_year = ? " +
                "AND position_label IN ('President','Vice_President','Treasurer','Secretary')";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            stmt.setInt(2, civilYear);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) == 4;
            return false;
        }
    }

    public boolean existsByNumber(String number) throws SQLException {
        String sql = "SELECT COUNT(*) FROM collective WHERE phone = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, number);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        }
    }

    public boolean existsByName(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM collective WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        }
    }

    public Collective assignIdentity(int collectiveId, String number, String name) throws SQLException {
        String sql = "UPDATE collective SET phone = ?, name = ? WHERE id_collective = ? RETURNING *";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, number);
            stmt.setString(2, name);
            stmt.setInt(3, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    private Collective mapRow(ResultSet rs) throws SQLException {
        Collective c = new Collective();
        c.setCollectiveId(rs.getInt("id_collective"));
        c.setFederationId(rs.getInt("id_federation"));
        c.setSpecialityId((Integer) rs.getObject("id_specialty"));
        c.setBranchId((Integer) rs.getObject("id_branch"));
        c.setName(rs.getString("name"));
        c.setNumber(rs.getString("phone"));
        c.setLocation(rs.getString("location"));
        c.setPhone(rs.getString("phone"));
        c.setCreationDate(rs.getDate("creation_date").toLocalDate());
        c.setPresidentId((Integer) rs.getObject("president_id"));
        return c;
    }
}