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
        String sql = "INSERT INTO collectif (id_federation, id_specialite, id_branche, nom, lieu_exercice, telephone, date_creation) " +
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
                collective.setCollectiveId(rs.getInt("id_collectif"));
            }
            return collective;
        }
    }

    public Collective findById(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM collectif WHERE id_collectif = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    public Collective findByIdWithMembers(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM collectif WHERE id_collectif = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    public int countActiveMembers(int collectiveId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM membre WHERE id_collectif = ? AND est_demissionne = FALSE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        }
    }

    public int countMembersWithSixMonthsSeniority(int collectiveId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM membre " +
                "WHERE id_collectif = ? AND est_demissionne = FALSE " +
                "AND date_adhesion <= CURRENT_DATE - INTERVAL '6 months'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        }
    }

    public boolean hasAllSpecificPositionsFilled(int collectiveId, int civilYear) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT libelle_poste) FROM poste_mandat " +
                "WHERE id_collectif = ? AND annee_civile = ? " +
                "AND libelle_poste IN ('Président','Président adjoint','Trésorier','Secrétaire')";
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
        String sql = "SELECT COUNT(*) FROM collectif WHERE numero = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, number);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        }
    }

    public boolean existsByName(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM collectif WHERE nom = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        }
    }

    public Collective assignIdentity(int collectiveId, String number, String name) throws SQLException {
        String sql = "UPDATE collectif SET numero = ?, nom = ? WHERE id_collectif = ? RETURNING *";
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
        c.setCollectiveId(rs.getInt("id_collectif"));
        c.setFederationId(rs.getInt("id_federation"));
        c.setSpecialityId((Integer) rs.getObject("id_specialite"));
        c.setBranchId((Integer) rs.getObject("id_branche"));
        c.setName(rs.getString("nom"));
        c.setNumber(rs.getString("numero"));
        c.setLocation(rs.getString("lieu_exercice"));
        c.setPhone(rs.getString("telephone"));
        c.setCreationDate(rs.getDate("date_creation").toLocalDate());
        c.setPresidentId((Integer) rs.getObject("id_president"));
        return c;
    }
}