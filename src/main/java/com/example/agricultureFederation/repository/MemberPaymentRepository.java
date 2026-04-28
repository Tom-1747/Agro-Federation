package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.MemberPayment;
import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberPaymentRepository {

    private final DataSource dataSource;

    public MemberPaymentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MemberPayment save(MemberPayment payment) throws SQLException {
        String sql = """
            INSERT INTO membershipfees
                (id_contribution, id_account, amount, membershipfees_date, payment_method)
            VALUES (?, ?, ?, ?, ?::payment_method_type)
            RETURNING id_membershipfees
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getContributionId());
            stmt.setInt(2, payment.getAccountId());
            stmt.setBigDecimal(3, payment.getAmount());
            stmt.setDate(4, Date.valueOf(payment.getPaymentDate()));
            stmt.setString(5, mapPaymentMethod(payment.getPaymentMethod()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                payment.setPaymentId(rs.getInt("id_membershipfees"));
            }
            return payment;
        }
    }

    public List<MemberPayment> findByMemberId(int memberId) throws SQLException {
        String sql = """
            SELECT 
                mf.id_membershipfees,
                mf.id_contribution,
                mf.id_account,
                mf.amount,
                mf.membershipfees_date,
                mf.payment_method,
                c.id_member,
                c.id_collective
            FROM membershipfees mf
            JOIN contribution c ON mf.id_contribution = c.id_contribution
            WHERE c.id_member = ?
            ORDER BY mf.membershipfees_date DESC
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            List<MemberPayment> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRowWithMember(rs));
            }
            return list;
        }
    }

    public List<MemberPayment> findByCollectiveId(int collectiveId) throws SQLException {
        String sql = """
            SELECT 
                mf.id_membershipfees,
                mf.id_contribution,
                mf.id_account,
                mf.amount,
                mf.membershipfees_date,
                mf.payment_method,
                c.id_member,
                c.id_collective
            FROM membershipfees mf
            JOIN contribution c ON mf.id_contribution = c.id_contribution
            WHERE c.id_collective = ?
            ORDER BY mf.membershipfees_date DESC
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<MemberPayment> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRowWithMember(rs));
            }
            return list;
        }
    }

    public List<MemberPayment> findByContributionId(int contributionId) throws SQLException {
        String sql = """
            SELECT 
                id_membershipfees,
                id_contribution,
                id_account,
                amount,
                membershipfees_date,
                payment_method
            FROM membershipfees
            WHERE id_contribution = ?
            ORDER BY membershipfees_date DESC
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contributionId);
            ResultSet rs = stmt.executeQuery();
            List<MemberPayment> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public List<MemberPayment> findByAccountId(int accountId) throws SQLException {
        String sql = """
            SELECT 
                id_membershipfees,
                id_contribution,
                id_account,
                amount,
                membershipfees_date,
                payment_method
            FROM membershipfees
            WHERE id_account = ?
            ORDER BY membershipfees_date DESC
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            List<MemberPayment> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public MemberPayment findById(int paymentId) throws SQLException {
        String sql = """
            SELECT 
                id_membershipfees,
                id_contribution,
                id_account,
                amount,
                membershipfees_date,
                payment_method
            FROM membershipfees
            WHERE id_membershipfees = ?
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    public BigDecimal getTotalPaidByMember(int memberId) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(mf.amount), 0)
            FROM membershipfees mf
            JOIN contribution c ON mf.id_contribution = c.id_contribution
            WHERE c.id_member = ?
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        }
    }

    public boolean deleteById(int paymentId) throws SQLException {
        String sql = "DELETE FROM membershipfees WHERE id_membershipfees = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    private MemberPayment mapRow(ResultSet rs) throws SQLException {
        MemberPayment payment = new MemberPayment();
        payment.setPaymentId(rs.getInt("id_membershipfees"));
        payment.setContributionId(rs.getInt("id_contribution"));
        payment.setAccountId(rs.getInt("id_account"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentMethod(reverseMapPaymentMethod(rs.getString("payment_method")));
        payment.setPaymentDate(rs.getDate("membershipfees_date").toLocalDate());
        return payment;
    }

    private MemberPayment mapRowWithMember(ResultSet rs) throws SQLException {
        MemberPayment payment = mapRow(rs);
        payment.setMemberId(rs.getInt("id_member"));
        payment.setCollectiveId(rs.getInt("id_collective"));
        return payment;
    }

    private String mapPaymentMethod(PaymentMethodType method) {
        if (method == null) return "Cash";
        return switch (method) {
            case Cash -> "Cash";
            case Bank_Transfer -> "Bank_Transfer";
            case Mobile_Money -> "Mobile_Money";
        };
    }

    private PaymentMethodType reverseMapPaymentMethod(String dbValue) {
        if (dbValue == null) return null;
        return switch (dbValue) {
            case "Cash" -> PaymentMethodType.Cash;
            case "Bank_Transfer" -> PaymentMethodType.Bank_Transfer;
            case "Mobile_Money" -> PaymentMethodType.Mobile_Money;
            default -> null;
        };
    }
}