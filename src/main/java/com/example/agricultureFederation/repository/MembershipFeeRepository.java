package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.MembershipFee;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembershipFeeRepository {

    private final DataSource dataSource;

    public MembershipFeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MembershipFee> findByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM cotisation WHERE id_collectif = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<MembershipFee> fees = new ArrayList<>();
            while (rs.next()) fees.add(mapRow(rs));
            return fees;
        }
    }

    public MembershipFee save(MembershipFee fee) throws SQLException {
        String sql = "INSERT INTO cotisation (id_collectif, type_cotisation, periodicite, montant, date_echeance, est_payee) " +
                "VALUES (?, ?::type_cotisation_type, ?::periodicite_type, ?, ?, FALSE) RETURNING *";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fee.getCollectiveId());
            stmt.setString(2, mapFrequencyToType(fee.getFrequency()));
            stmt.setString(3, mapFrequencyToPeriodicity(fee.getFrequency()));
            stmt.setDouble(4, fee.getAmount());
            stmt.setDate(5, Date.valueOf(fee.getEligibleFrom()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    public MembershipFee findById(int id) throws SQLException {
        String sql = "SELECT * FROM cotisation WHERE id_cotisation = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    private String mapFrequencyToType(String frequency) {
        return switch (frequency) {
            case "MONTHLY", "ANNUALLY" -> "Périodique";
            default -> "Ponctuelle";
        };
    }

    private String mapFrequencyToPeriodicity(String frequency) {
        return switch (frequency) {
            case "MONTHLY" -> "Mensuelle";
            case "ANNUALLY" -> "Annuelle";
            default -> null;
        };
    }

    private MembershipFee mapRow(ResultSet rs) throws SQLException {
        MembershipFee fee = new MembershipFee();
        fee.setMembershipFeeId(rs.getInt("id_cotisation"));
        fee.setCollectiveId(rs.getInt("id_collectif"));
        fee.setEligibleFrom(rs.getDate("date_echeance").toLocalDate());
        fee.setAmount(rs.getDouble("montant"));
        fee.setStatus(rs.getBoolean("est_payee") ? "ACTIVE" : "INACTIVE");
        return fee;
    }
}