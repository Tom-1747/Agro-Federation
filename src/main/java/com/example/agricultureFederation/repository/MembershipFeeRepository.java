package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.MembershipFee;
import com.example.agricultureFederation.entity.enums.ContributionTypeType;
import com.example.agricultureFederation.entity.enums.FrequencyType;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MembershipFeeRepository {

    private final DataSource dataSource;

    public MembershipFeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MembershipFee save(MembershipFee fee) throws SQLException {
        String sql = """
            INSERT INTO contribution
                (id_member, id_collective, contribution_type, frequency, amount, due_date, is_paid)
            VALUES (?, ?, ?::contribution_type_type, ?::frequency_type, ?, ?, FALSE)
            RETURNING id_contribution
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fee.getCollectiveId());
            stmt.setInt(2, fee.getCollectiveId());
            stmt.setString(3, mapContributionType(fee.getContributionType()));
            stmt.setString(4, mapFrequencyType(fee.getFrequency()));
            stmt.setBigDecimal(5, fee.getAmount());
            stmt.setDate(6, fee.getDueDate() != null ? Date.valueOf(fee.getDueDate()) : null);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fee.setMembershipFeeId(rs.getInt("id_contribution"));
            }
            return fee;
        }
    }

    public MembershipFee saveForMember(MembershipFee fee, int memberId) throws SQLException {
        String sql = """
            INSERT INTO contribution
                (id_member, id_collective, contribution_type, frequency, amount, due_date, is_paid)
            VALUES (?, ?, ?::contribution_type_type, ?::frequency_type, ?, ?, FALSE)
            RETURNING id_contribution
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.setInt(2, fee.getCollectiveId());
            stmt.setString(3, mapContributionType(fee.getContributionType()));
            stmt.setString(4, mapFrequencyType(fee.getFrequency()));
            stmt.setBigDecimal(5, fee.getAmount());
            stmt.setDate(6, fee.getDueDate() != null ? Date.valueOf(fee.getDueDate()) : null);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fee.setMembershipFeeId(rs.getInt("id_contribution"));
                fee.setMemberId(memberId);
            }
            return fee;
        }
    }

    public List<MembershipFee> findByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM contribution WHERE id_collective = ? ORDER BY due_date";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<MembershipFee> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public MembershipFee findById(int id) throws SQLException {
        String sql = "SELECT * FROM contribution WHERE id_contribution = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    public List<MembershipFee> findByMemberId(int memberId) throws SQLException {
        String sql = "SELECT * FROM contribution WHERE id_member = ? ORDER BY due_date DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            List<MembershipFee> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public List<MembershipFee> findUnpaidByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM contribution WHERE id_collective = ? AND is_paid = FALSE ORDER BY due_date";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<MembershipFee> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public List<MembershipFee> findUnpaidByMemberId(int memberId) throws SQLException {
        String sql = "SELECT * FROM contribution WHERE id_member = ? AND is_paid = FALSE ORDER BY due_date";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            List<MembershipFee> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public List<MembershipFee> findOverdueByCollectiveId(int collectiveId) throws SQLException {
        String sql = """
            SELECT * FROM contribution 
            WHERE id_collective = ? AND is_paid = FALSE AND due_date < CURRENT_DATE 
            ORDER BY due_date
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<MembershipFee> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public void updatePaymentStatus(int contributionId, boolean isPaid) throws SQLException {
        String sql = "UPDATE contribution SET is_paid = ? WHERE id_contribution = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isPaid);
            stmt.setInt(2, contributionId);
            stmt.executeUpdate();
        }
    }

    public void assignToMember(int contributionId, int memberId) throws SQLException {
        String sql = "UPDATE contribution SET id_member = ? WHERE id_contribution = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            stmt.setInt(2, contributionId);
            stmt.executeUpdate();
        }
    }

    public BigDecimal getTotalContributionsByCollective(int collectiveId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM contribution WHERE id_collective = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalPaidByCollective(int collectiveId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM contribution WHERE id_collective = ? AND is_paid = TRUE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        }
    }

    public boolean deleteById(int contributionId) throws SQLException {
        String sql = "DELETE FROM contribution WHERE id_contribution = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contributionId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    private MembershipFee mapRow(ResultSet rs) throws SQLException {
        MembershipFee fee = new MembershipFee();
        fee.setMembershipFeeId(rs.getInt("id_contribution"));
        fee.setMemberId(rs.getInt("id_member"));
        fee.setCollectiveId(rs.getInt("id_collective"));

        String contributionType = rs.getString("contribution_type");
        if (contributionType != null) {
            fee.setContributionType(ContributionTypeType.valueOf(contributionType.toUpperCase().replace("-", "_")));
        }

        String frequency = rs.getString("frequency");
        if (frequency != null) {
            fee.setFrequency(FrequencyType.valueOf(frequency.toUpperCase()));
        }

        fee.setAmount(rs.getBigDecimal("amount"));
        fee.setDueDate(rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null);
        fee.setIsPaid(rs.getBoolean("is_paid"));
        return fee;
    }

    private String mapContributionType(ContributionTypeType type) {
        if (type == null) return "One_time";
        return switch (type) {
            case One_time -> "One_time";
            case Periodic -> "Periodic";
        };
    }

    private String mapFrequencyType(FrequencyType frequency) {
        if (frequency == null) return null;
        return switch (frequency) {
            case Monthly -> "Monthly";
            case Annual -> "Annual";
        };
    }
}