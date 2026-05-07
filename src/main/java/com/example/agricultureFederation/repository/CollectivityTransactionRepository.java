package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.CollectivityTransaction;
import com.example.agricultureFederation.entity.enums.PaymentMethodType;

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
            INSERT INTO collectivity_transaction
                (id_collective, id_member, id_account, amount, creation_date, payment_method)
            VALUES (?, ?, ?, ?, ?, ?::payment_method_type)
            RETURNING id_transaction
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getCollectiveId());
            stmt.setInt(2, transaction.getMemberId());
            stmt.setInt(3, transaction.getAccountId());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setDate(5, Date.valueOf(transaction.getCreationDate()));
            stmt.setString(6, mapPaymentMethod(transaction.getPaymentMethod()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) transaction.setTransactionId(rs.getInt("id_transaction"));
            return transaction;
        }
    }

    public List<CollectivityTransaction> findByCollectiveIdAndPeriod(
            int collectiveId, LocalDate from, LocalDate to) throws SQLException {

        String sql = """
            SELECT
                ct.id_transaction,
                ct.id_collective,
                ct.id_member,
                ct.id_account,
                ct.amount,
                ct.creation_date,
                ct.payment_method
            FROM collectivity_transaction ct
            WHERE ct.id_collective = ?
              AND ct.creation_date BETWEEN ? AND ?
            ORDER BY ct.creation_date DESC
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
        t.setTransactionId(rs.getInt("id_transaction"));
        t.setCollectiveId(rs.getInt("id_collective"));
        t.setMemberId(rs.getInt("id_member"));
        t.setAccountId(rs.getInt("id_account"));
        t.setAmount(rs.getBigDecimal("amount"));
        t.setPaymentMethod(reverseMapPaymentMethod(rs.getString("payment_method")));
        t.setCreationDate(rs.getDate("creation_date").toLocalDate());
        return t;
    }

    private String mapPaymentMethod(PaymentMethodType method) {
        if (method == null) return "CASH";
        return switch (method) {
            case CASH -> "CASH";
            case BANK_TRANSFER -> "BANK_TRANSFER";
            case MOBILE_BANKING -> "MOBILE_BANKING";
        };
    }

    private PaymentMethodType reverseMapPaymentMethod(String dbValue) {
        if (dbValue == null) return null;
        return switch (dbValue) {
            case "CASH" -> PaymentMethodType.CASH;
            case "BANK_TRANSFER" -> PaymentMethodType.BANK_TRANSFER;
            case "MOBILE_BANKING" -> PaymentMethodType.MOBILE_BANKING;
            default -> null;
        };
    }
}