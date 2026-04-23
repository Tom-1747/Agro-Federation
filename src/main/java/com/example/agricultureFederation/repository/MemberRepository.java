package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.Member;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {

    private final DataSource dataSource;

    public MemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = """
            INSERT INTO member 
                (id_collective, id_trade, last_name, first_name, birth_date, 
                 gender, address, phone, email, join_date, is_resigned)
            VALUES (?, ?, ?, ?, ?, ?::gender_type, ?, ?, ?, ?, FALSE)
            RETURNING id_member
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, member.getCollectiveId());
            stmt.setObject(2, member.getJobId());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getFirstName());
            stmt.setDate(5, Date.valueOf(member.getBirthDate()));
            stmt.setString(6, member.getGender());
            stmt.setString(7, member.getAddress());
            stmt.setString(8, member.getPhone());
            stmt.setString(9, member.getEmail());
            stmt.setDate(10, Date.valueOf(member.getMembershipDate()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                member.setMemberId(rs.getInt("id_member"));
            }
            return member;
        }
    }

    public Member findById(int memberId) throws SQLException {
        String sql = "SELECT * FROM member WHERE id_member = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    public List<Member> findActiveByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM member WHERE id_collective = ? AND is_resigned = FALSE ORDER BY last_name, first_name";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(mapRow(rs));
            }
            return members;
        }
    }

    public List<Member> findAllByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM member WHERE id_collective = ? ORDER BY last_name, first_name";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(mapRow(rs));
            }
            return members;
        }
    }

    public List<Member> findAll() throws SQLException {
        String sql = "SELECT * FROM member ORDER BY id_collective, last_name, first_name";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(mapRow(rs));
            }
            return members;
        }
    }

    public boolean isConfirmedMember(int memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM position_mandate " +
                "WHERE id_member = ? AND position_label = 'Confirmed Member'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public boolean hasMinimumSeniority(int memberId, int minimumDays) throws SQLException {
        String sql = "SELECT COUNT(*) FROM member " +
                "WHERE id_member = ? AND is_resigned = FALSE " +
                "AND join_date <= CURRENT_DATE - (? * INTERVAL '1 day')";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, minimumDays);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public List<Member> findByIds(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String placeholders = String.join(",", ids.stream().map(id -> "?").toArray(String[]::new));
        String sql = "SELECT * FROM member WHERE id_member IN (" + placeholders + ") AND is_resigned = FALSE";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                stmt.setInt(i + 1, ids.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(mapRow(rs));
            }
            return members;
        }
    }

    public int countActiveMembersByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM member WHERE id_collective = ? AND is_resigned = FALSE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public int countMembersWithMinSeniority(int collectiveId, int months) throws SQLException {
        String sql = "SELECT COUNT(*) FROM member " +
                "WHERE id_collective = ? AND is_resigned = FALSE " +
                "AND join_date <= CURRENT_DATE - (? * INTERVAL '1 month')";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            stmt.setInt(2, months);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public Member update(Member member) throws SQLException {
        String sql = """
            UPDATE member 
            SET last_name = ?, first_name = ?, birth_date = ?, gender = ?::gender_type,
                address = ?, phone = ?, email = ?, is_resigned = ?
            WHERE id_member = ?
            RETURNING *
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getLastName());
            stmt.setString(2, member.getFirstName());
            stmt.setDate(3, Date.valueOf(member.getBirthDate()));
            stmt.setString(4, member.getGender());
            stmt.setString(5, member.getAddress());
            stmt.setString(6, member.getPhone());
            stmt.setString(7, member.getEmail());
            stmt.setBoolean(8, member.isResigned());
            stmt.setInt(9, member.getMemberId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return member;
        }
    }

    public boolean resignMember(int memberId) throws SQLException {
        String sql = "UPDATE member SET is_resigned = TRUE WHERE id_member = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean deleteById(int memberId) throws SQLException {
        String sql = "DELETE FROM member WHERE id_member = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM member WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public boolean existsByPhone(String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM member WHERE phone = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("id_member"));
        member.setCollectiveId(rs.getInt("id_collective"));
        member.setJobId((Integer) rs.getObject("id_trade"));
        member.setLastName(rs.getString("last_name"));
        member.setFirstName(rs.getString("first_name"));
        member.setBirthDate(rs.getDate("birth_date").toLocalDate());
        member.setGender(rs.getString("gender"));
        member.setAddress(rs.getString("address"));
        member.setPhone(rs.getString("phone"));
        member.setEmail(rs.getString("email"));
        member.setMembershipDate(rs.getDate("join_date").toLocalDate());
        member.setResigned(rs.getBoolean("is_resigned"));
        return member;
    }
}