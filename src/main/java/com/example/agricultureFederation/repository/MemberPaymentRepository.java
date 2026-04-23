package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.MemberPayment;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberPaymentRepository {

    private final DataSource dataSource;

    public MemberPaymentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MemberPayment save(MemberPayment payment) throws SQLException {
        String sql = "INSERT INTO encaissement (id_cotisation, id_compte, montant, date_encaissement, mode_paiement) " +
                "VALUES (?, ?, ?, ?, ?::mode_paiement_type) RETURNING *";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getMembershipFeeId());
            stmt.setInt(2, payment.getAccountId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setDate(4, Date.valueOf(payment.getCreationDate()));
            stmt.setString(5, mapPaymentMode(payment.getPaymentMode()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                payment.setPaymentId(rs.getInt("id_encaissement"));
            }
            return payment;
        }
    }

    private String mapPaymentMode(String mode) {
        return switch (mode) {
            case "CASH" -> "Espèces";
            case "MOBILE_BANKING" -> "Mobile Money";
            case "BANK_TRANSFER" -> "Virement bancaire";
            default -> throw new IllegalArgumentException("Unknown payment mode: " + mode);
        };
    }
}