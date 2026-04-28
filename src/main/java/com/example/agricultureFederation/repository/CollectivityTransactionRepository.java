package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.CollectivityTransaction;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CollectivityTransactionRepository {

    private final DataSource dataSource;

    public CollectivityTransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public CollectivityTransaction save(CollectivityTransaction transaction) throws SQLException {
        String sql = """
            INSERT INTO "membershipfees"
                (id_contribution, id_account, amount, membershipFees_date, payment_method)
            VALUES (?, ?, ?, ?, ?::payment_method_type)
            RETURNING id_membershipfees
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // id_contribution est l'ID de la cotisation liée au membre
            stmt.setInt(1, transaction.getMemberId());   // contribution.id_contribution ≈ memberId ici
            stmt.setInt(2, transaction.getAccountId());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setDate(4, Date.valueOf(transaction.getCreationDate()));
            stmt.setString(5, mapPaymentMode(transaction.getPaymentMode()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) transaction.setTransactionId(rs.getInt("id_collection"));
            return transaction;
        }
    }


    public List<CollectivityTransaction> findByCollectiveIdAndPeriod(
            int collectiveId, LocalDate from, LocalDate to) throws SQLException {

        String sql = """
            SELECT
                mf.id_membershipfees,
                mf.id_contribution,
                mf.id_account,
                mf.amount,
                mf.membershipFees_date,
                mf.payment_method,
                c.id_member,
                c.id_collective
            FROM "membershipfees" mf
            JOIN contribution c ON mf.id_contribution = c.id_contribution
            WHERE c.id_collective = ?
              AND mf.membershipFees_date BETWEEN ? AND ?
            ORDER BY mf.membershipFees_date DESC
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, collectiveId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));

            ResultSet rs = stmt.executeQuery();
            List<CollectivityTransaction> list = new ArrayList<>();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        }
    }

    private CollectivityTransaction mapRow(ResultSet rs) throws SQLException {
        CollectivityTransaction t = new CollectivityTransaction();
        t.setTransactionId(rs.getInt("id_collection"));
        t.setCollectiveId(rs.getInt("id_collective"));
        t.setMemberId(rs.getInt("id_member"));
        t.setAccountId(rs.getInt("id_account"));
        t.setAmount(rs.getBigDecimal("amount"));
        t.setPaymentMode(reverseMapPaymentMode(rs.getString("payment_method")));
        t.setCreationDate(rs.getDate("membershipFees_date").toLocalDate());
        return t;
    }


    private String mapPaymentMode(String mode) {
        if (mode == null) return "Cash";
        return switch (mode.toUpperCase()) {
            case "CASH"          -> "Cash";
            case "BANK_TRANSFER" -> "Bank Transfer";
            case "MOBILE_MONEY", "MOBILE_BANKING" -> "Mobile Money";
            default -> "Cash";
        };
    }

    private String reverseMapPaymentMode(String dbValue) {
        if (dbValue == null) return null;
        return switch (dbValue) {
            case "Cash"          -> "CASH";
            case "Bank Transfer" -> "BANK_TRANSFER";
            case "Mobile Money"  -> "MOBILE_MONEY";
            default -> dbValue;
        };
    }
}