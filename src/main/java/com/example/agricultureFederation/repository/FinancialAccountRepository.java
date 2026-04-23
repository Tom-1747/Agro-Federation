package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.FinancialAccount;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FinancialAccountRepository {

    private final DataSource dataSource;

    public FinancialAccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public FinancialAccount findById(int accountId) throws SQLException {
        String sql = "SELECT * FROM account WHERE id_account = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        }
    }

    public List<FinancialAccount> findByCollectiveId(int collectiveId) throws SQLException {
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

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }


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
