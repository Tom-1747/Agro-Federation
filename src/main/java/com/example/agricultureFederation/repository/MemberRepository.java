package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD

=======
>>>>>>> 21-april
public class MemberRepository {

    private final DataSource dataSource;

    public MemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Member save(Member member) throws SQLException {
        String sql = """
            INSERT INTO member
                (id_collective, id_trade, last_name, first_name,
                 birth_date, gender, address, phone, email, join_date, is_resigned)
            VALUES (?, ?, ?, ?, ?, ?::gender_type, ?, ?, ?, ?, FALSE)
            RETURNING id_member
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, member.getCollectiveId());
            stmt.setObject(2, member.getJobId());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getFirstName());
            stmt.setDate(5, member.getBirthDate() != null ? Date.valueOf(member.getBirthDate()) : null);
            stmt.setString(6, member.getGender());
            stmt.setString(7, member.getAddress());
            stmt.setString(8, member.getPhone());
            stmt.setString(9, member.getEmail());
            stmt.setDate(10, Date.valueOf(member.getMembershipDate()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) member.setMemberId(rs.getInt("id_member"));
            return member;
        }
    }


    public Member findById(int memberId) throws SQLException {
        String sql = "SELECT * FROM member WHERE id_member = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        }
    }

    public List<Member> findByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM member WHERE id_collective = ? AND is_resigned = FALSE ORDER BY id_member";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<Member> list = new ArrayList<>();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        }
    }

    public List<Member> findByIds(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        String placeholders = "?,".repeat(ids.size());
        placeholders = placeholders.substring(0, placeholders.length() - 1);
        String sql = "SELECT * FROM member WHERE id_member IN (" + placeholders + ") AND is_resigned = FALSE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) stmt.setInt(i + 1, ids.get(i));
            ResultSet rs = stmt.executeQuery();
            List<Member> list = new ArrayList<>();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        }
    }

    public List<Member> findActiveByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM membre WHERE id_collectif = ? AND est_demissionne = FALSE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) members.add(mapRow(rs));
            return members;
        }
    }

    public boolean isConfirmedMember(int memberId) throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM position_mandate
            WHERE id_member = ?
              AND position_label = 'Confirmed Member'
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public boolean hasMinimumSeniority(int memberId, int minimumDays) throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM member
            WHERE id_member = ?
              AND is_resigned = FALSE
              AND join_date <= CURRENT_DATE - (? * INTERVAL '1 day')
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.setInt(2, minimumDays);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setMemberId(rs.getInt("id_member"));
        m.setCollectiveId(rs.getInt("id_collective"));
        m.setJobId((Integer) rs.getObject("id_trade"));
        m.setLastName(rs.getString("last_name"));
        m.setFirstName(rs.getString("first_name"));
        if (rs.getDate("birth_date") != null) m.setBirthDate(rs.getDate("birth_date").toLocalDate());
        m.setGender(rs.getString("gender"));
        m.setAddress(rs.getString("address"));
        m.setPhone(rs.getString("phone"));
        m.setEmail(rs.getString("email"));
        m.setMembershipDate(rs.getDate("join_date").toLocalDate());
        m.setResigned(rs.getBoolean("is_resigned"));
        return m;
    }
}