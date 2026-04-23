package com.example.agricultureFederation.repository;


import com.example.agricultureFederation.entity.FinancialAccount;
import javax.sql.DataSource;
import java.sql.*;

public class FinancialAccountRepository {

    private final DataSource dataSource;

    public FinancialAccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public FinancialAccount findById(int accountId) throws SQLException {
        String sql = "SELECT * FROM compte WHERE id_compte = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    public void updateBalance(int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE compte SET solde = ? WHERE id_compte = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }

    private FinancialAccount mapRow(ResultSet rs) throws SQLException {
        FinancialAccount account = new FinancialAccount();
        account.setAccountId(rs.getInt("id_compte"));
        account.setCollectiveId(rs.getInt("id_collectif") );
        account.setType(rs.getString("type_compte"));
        account.setHolderName(rs.getString("titulaire_compte"));
        account.setBankName(rs.getString("nom_banque"));
        account.setMobileBankingService(rs.getString("service_mob_money"));
        account.setMobileNumber(rs.getString("numero_telephone"));
        account.setAmount(rs.getDouble("solde"));
        return account;
    }
}