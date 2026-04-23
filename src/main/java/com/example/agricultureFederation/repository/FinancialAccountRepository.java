package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.FinancialAccount;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinancialAccountRepository {

    private final DataSource dataSource;

    public FinancialAccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 3.1 Trouver un compte par ID
     * SELECT * FROM account WHERE id_account = ?
     */
    public FinancialAccount findById(int accountId) throws SQLException {
        String sql = "SELECT * FROM account WHERE id_account = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    /**
     * 3.2 Lister les comptes d'un collectif
     * SELECT * FROM account WHERE id_collective = ? ORDER BY id_account
     */
    public List<FinancialAccount> findByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM account WHERE id_collective = ? ORDER BY id_account";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<FinancialAccount> accounts = new ArrayList<>();
            while (rs.next()) {
                accounts.add(mapRow(rs));
            }
            return accounts;
        }
    }

    /**
     * 3.4 Calculer le solde cumulé d'un compte à une date donnée
     * SELECT COALESCE(SUM(amount), 0) FROM membershipfees
     * WHERE id_account = ? AND membershipfees_date <= ?
     */
    public double getBalanceAt(int accountId, LocalDate at) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM membershipfees " +
                "WHERE id_account = ? AND membershipfees_date <= ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.setDate(2, Date.valueOf(at));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0;
        }
    }

    /**
     * 3.3 Mettre à jour le solde d'un compte
     * UPDATE account SET balance = ? WHERE id_account = ?
     */
    public void updateBalance(int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE account SET balance = ? WHERE id_account = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }

    /**
     * Create a new account for a collective
     */
    public FinancialAccount save(FinancialAccount account) throws SQLException {
        String sql = """
            INSERT INTO account 
                (id_collective, account_type, account_holder, balance)
            VALUES (?, ?::account_type_type, ?, ?)
            RETURNING id_account
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, account.getCollectiveId());
            stmt.setString(2, account.getType());
            stmt.setString(3, account.getHolderName());
            stmt.setDouble(4, account.getAmount());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account.setAccountId(rs.getInt("id_account"));
            }
            return account;
        }
    }

    /**
     * Get total collected amount for an account (all time)
     */
    public double getTotalCollected(int accountId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM membershipfees WHERE id_account = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0;
        }
    }

    private FinancialAccount mapRow(ResultSet rs) throws SQLException {
        FinancialAccount account = new FinancialAccount();
        account.setAccountId(rs.getInt("id_account"));           // English: id_account
        account.setCollectiveId(rs.getInt("id_collective"));     // English: id_collective
        account.setType(rs.getString("account_type"));           // English: account_type
        account.setHolderName(rs.getString("account_holder"));   // English: account_holder
        account.setAmount(rs.getDouble("balance"));              // English: balance
        // Note: Your schema doesn't have bank_name, service_mob_money, or numero_telephone in account table
        // You may need to add these columns to your schema or remove them from FinancialAccount entity
        return account;
    }
}