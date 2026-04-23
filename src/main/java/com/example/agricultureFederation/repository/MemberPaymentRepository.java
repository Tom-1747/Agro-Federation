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
        String sql = """
            INSERT INTO "membershipfees"
                (id_contribution, id_account, amount, membershipFees_date, payment_method)
            VALUES (?, ?, ?, ?, ?::payment_method_type)
            RETURNING id_membershipfees
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getMembershipFeeId());  // id_contribution
            stmt.setInt(2, payment.getAccountId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setDate(4, Date.valueOf(payment.getCreationDate()));

            stmt.setString(5, mapPaymentMode(payment.getPaymentMode()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) payment.setPaymentId(rs.getInt("id_collection"));
            return payment;
        }
    }


    public List<MemberPayment> findByMemberId(int memberId) throws SQLException {

        String sql = """
            SELECT mf.*, c.id_member FROM "membershipfees" mf
            JOIN contribution c ON mf.id_contribution = c.id_contribution
            WHERE c.id_member = ?
            ORDER BY mf.membershipFees_date DESC
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            List<MemberPayment> list = new ArrayList<>();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        }
    }


    private MemberPayment mapRow(ResultSet rs) throws SQLException {
        MemberPayment p = new MemberPayment();
        p.setPaymentId(rs.getInt("id_collection"));
        p.setMembershipFeeId(rs.getInt("id_contribution"));
        p.setAccountId(rs.getInt("id_account"));
        p.setAmount(rs.getInt("amount"));
        p.setPaymentMode(reverseMapPaymentMode(rs.getString("payment_method")));
        p.setCreationDate(rs.getDate("membershipFees_date").toLocalDate());
        return p;
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
