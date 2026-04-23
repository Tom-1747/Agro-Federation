package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.FinancialAccount;
<<<<<<< HEAD

import javax.sql.DataSource;
import java.sql.*;
=======
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
>>>>>>> 21-april
import java.util.ArrayList;
import java.util.List;

public class FinancialAccountRepository {

    private final DataSource dataSource;

    public FinancialAccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public FinancialAccount findById(int accountId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT * FROM account WHERE id_account = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapRow(rs) : null;
=======
        String sql = "SELECT * FROM compte WHERE id_compte = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
>>>>>>> 21-april
        }
    }

    public List<FinancialAccount> findByCollectiveId(int collectiveId) throws SQLException {
<<<<<<< HEAD
        String sql = "SELECT * FROM account WHERE id_collective = ? ORDER BY id_account";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<FinancialAccount> list = new ArrayList<>();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        }
    }


    public void updateBalance(int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE account SET balance = ? WHERE id_account = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

=======
        String sql = "SELECT * FROM compte WHERE id_collectif = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<FinancialAccount> accounts = new ArrayList<>();
            while (rs.next()) accounts.add(mapRow(rs));
            return accounts;
        }
    }

    public double getBalanceAt(int accountId, LocalDate at) throws SQLException {
        String sql = "SELECT COALESCE(SUM(montant), 0) FROM encaissement " +
                "WHERE id_compte = ? AND date_encaissement <= ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.setDate(2, Date.valueOf(at));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
            return 0;
        }
    }

    public void updateBalance(int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE compte SET solde = ? WHERE id_compte = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
>>>>>>> 21-april
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }

<<<<<<< HEAD

    private FinancialAccount mapRow(ResultSet rs) throws SQLException {
        FinancialAccount a = new FinancialAccount();
        a.setAccountId(rs.getInt("id_account"));
        a.setCollectiveId(rs.getInt("id_collective"));
        a.setType(rs.getString("account_type"));           // 'Cash','Bank','Mobile Money'
        a.setHolderName(rs.getString("account_holder"));
        a.setBankName(rs.getString("bank_name"));          // bank_name_type
        a.setMobileBankingService(rs.getString("mobile_money_service")); // mobile_money_type
        a.setMobileNumber(rs.getString("phone_number"));   // VARCHAR en base
        a.setAmount(rs.getDouble("balance"));
        // bank_account_number est VARCHAR(23) en base — pas de bankCode/branchCode séparés
        a.setBankAccountNumber(parseBankAccountNumber(rs.getString("bank_account_number")));
        return a;
    }

    private Integer parseBankAccountNumber(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try { return Integer.parseInt(raw.trim()); } catch (NumberFormatException e) { return null; }
    }
}
=======
    private FinancialAccount mapRow(ResultSet rs) throws SQLException {
        FinancialAccount account = new FinancialAccount();
        account.setAccountId(rs.getInt("id_compte"));
        account.setCollectiveId(rs.getInt("id_collectif"));
        account.setType(rs.getString("type_compte"));
        account.setHolderName(rs.getString("titulaire_compte"));
        account.setBankName(rs.getString("nom_banque"));
        account.setMobileBankingService(rs.getString("service_mob_money"));
        account.setMobileNumber(rs.getString("numero_telephone"));
        account.setAmount(rs.getDouble("solde"));
        return account;
    }
}
>>>>>>> 21-april
