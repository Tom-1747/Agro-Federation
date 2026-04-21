package repository;

import entity.Member;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {

    private final DataSource dataSource;

    public MemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "INSERT INTO membre (id_collectif, id_metier, nom, prenom, date_naissance, genre, adresse, telephone, email, date_adhesion, est_demissionne) " +
                "VALUES (?, ?, ?, ?, ?, ?::genre_type, ?, ?, ?, ?, FALSE) RETURNING *";
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
                member.setMemberId(rs.getInt("id_membre"));
            }
            return member;
        }
    }

    public Member findById(int memberId) throws SQLException {
        String sql = "SELECT * FROM membre WHERE id_membre = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    public boolean isConfirmedMember(int memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM poste_mandat " +
                "WHERE id_membre = ? AND libelle_poste = 'Membre confirmé'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        }
    }

    public boolean hasMinimumSeniority(int memberId, int minimumDays) throws SQLException {
        String sql = "SELECT COUNT(*) FROM membre " +
                "WHERE id_membre = ? AND est_demissionne = FALSE " +
                "AND date_adhesion <= CURRENT_DATE - INTERVAL '" + minimumDays + " days'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        }
    }

    public List<Member> findSponsorsByIds(List<Integer> ids) throws SQLException {
        String placeholders = ids.stream()
                .map(id -> "?")
                .reduce((a, b) -> a + "," + b)
                .orElse("0");
        String sql = "SELECT * FROM membre WHERE id_membre IN (" + placeholders + ") AND est_demissionne = FALSE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                stmt.setInt(i + 1, ids.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) members.add(mapRow(rs));
            return members;
        }
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setMemberId(rs.getInt("id_membre"));
        m.setCollectiveId(rs.getInt("id_collectif"));
        m.setJobId((Integer) rs.getObject("id_metier"));
        m.setLastName(rs.getString("nom"));
        m.setFirstName(rs.getString("prenom"));
        m.setBirthDate(rs.getDate("date_naissance").toLocalDate());
        m.setGender(rs.getString("genre"));
        m.setAddress(rs.getString("adresse"));
        m.setPhone(rs.getString("telephone"));
        m.setEmail(rs.getString("email"));
        m.setMembershipDate(rs.getDate("date_adhesion").toLocalDate());
        m.setResigned(rs.getBoolean("est_demissionne"));
        return m;
    }
}
