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


    public MembershipFee saveContribution(MembershipFee fee) throws SQLException {
        String sql = """
            INSERT INTO contribution
                (id_member, id_collective, contribution_type, frequency, amount, due_date, is_paid)
            VALUES (?, ?, ?::contribution_type_type, ?::frequency_type, ?, ?, FALSE)
            RETURNING id_contribution
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // id_member = 0 pour une cotisation collective (pas encore assignée à un membre)
            stmt.setInt(1, fee.getCollectiveId());   // utilisé comme id_member temporaire
            stmt.setInt(2, fee.getCollectiveId());
            stmt.setString(3, mapFrequencyToContributionType(fee.getFrequency()));
            stmt.setString(4, mapFrequencyType(fee.getFrequency()));
            stmt.setDouble(5, fee.getAmount());
            stmt.setDate(6, fee.getEligibleFrom() != null ? Date.valueOf(fee.getEligibleFrom()) : null);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) fee.setMembershipFeeId(rs.getInt("id_contribution"));
            return fee;
        }
    }

    public List<MembershipFee> findByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM contribution WHERE id_collective = ? ORDER BY id_contribution";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<MembershipFee> list = new ArrayList<>();
            while (rs.next()) list.add(mapContributionRow(rs));
            return list;
        }
    }

    public MembershipFee findById(int id) throws SQLException {
        String sql = "SELECT * FROM contribution WHERE id_contribution = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapContributionRow(rs) : null;
        }
    }


    public int saveEncaissement(int idContribution, int idAccount,
                                double amount, java.time.LocalDate date,
                                String paymentMethod) throws SQLException {
        String sql = """
            INSERT INTO "membershipfees"
                (id_contribution, id_account, amount, membershipFees_date, payment_method)
            VALUES (?, ?, ?, ?, ?::payment_method_type)
            RETURNING id_membershipfees
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idContribution);
            stmt.setInt(2, idAccount);
            stmt.setDouble(3, amount);
            stmt.setDate(4, Date.valueOf(date));
            stmt.setString(5, paymentMethod);

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id_collection") : -1;
        }
    }

    private MembershipFee mapContributionRow(ResultSet rs) throws SQLException {
        MembershipFee fee = new MembershipFee();
        fee.setMembershipFeeId(rs.getInt("id_contribution"));
        fee.setCollectiveId(rs.getInt("id_collective"));
        if (rs.getDate("due_date") != null) fee.setEligibleFrom(rs.getDate("due_date").toLocalDate());
        fee.setFrequency(rs.getString("frequency"));
        fee.setAmount(rs.getDouble("amount"));
        fee.setStatus(rs.getBoolean("is_paid") ? "ACTIVE" : "INACTIVE");
        return fee;
    }

    private String mapFrequencyToContributionType(String frequency) {
        if (frequency == null) return "One-time";
        return switch (frequency.toUpperCase()) {
            case "MONTHLY", "ANNUAL", "WEEKLY" -> "Periodic";
            default -> "One-time";
        };
    }

    private String mapFrequencyType(String frequency) {
        if (frequency == null) return null;
        return switch (frequency.toUpperCase()) {
            case "MONTHLY" -> "Monthly";
            case "ANNUAL", "ANNUALLY" -> "Annual";
            default -> null;
        };
    }
}
