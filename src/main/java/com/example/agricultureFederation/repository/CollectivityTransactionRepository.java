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

    public List<CollectivityTransaction> findByCollectiveIdAndPeriod(
            int collectiveId, LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT e.*, c.id_membre FROM encaissement e " +
                "JOIN cotisation c ON e.id_cotisation = c.id_cotisation " +
                "WHERE c.id_collectif = ? " +
                "AND e.date_encaissement BETWEEN ? AND ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            List<CollectivityTransaction> transactions = new ArrayList<>();
            while (rs.next()) transactions.add(mapRow(rs, collectiveId));
            return transactions;
        }
    }

    public CollectivityTransaction save(CollectivityTransaction transaction) throws SQLException {
        String sql = "INSERT INTO encaissement (id_cotisation, id_compte, montant, date_encaissement, mode_paiement) " +
                "VALUES (?, ?, ?, ?, ?::mode_paiement_type) RETURNING *";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transaction.getMemberId());
            stmt.setInt(2, transaction.getAccountId());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setDate(4, Date.valueOf(transaction.getCreationDate()));
            stmt.setString(5, transaction.getPaymentMode());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                transaction.setTransactionId(rs.getInt("id_encaissement"));
            }
            return transaction;
        }
    }

    private CollectivityTransaction mapRow(ResultSet rs, int collectiveId) throws SQLException {
        CollectivityTransaction t = new CollectivityTransaction();
        t.setTransactionId(rs.getInt("id_encaissement"));
        t.setCollectiveId(collectiveId);
        t.setMemberId(rs.getInt("id_membre"));
        t.setAccountId(rs.getInt("id_compte"));
        t.setAmount(rs.getDouble("montant"));
        t.setCreationDate(rs.getDate("date_encaissement").toLocalDate());
        return t;
    }
}