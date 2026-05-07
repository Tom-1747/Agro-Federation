package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.Attendance;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRepository {

    private final DataSource dataSource;

    public AttendanceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Attendance save(Attendance attendance) throws SQLException {
        String sql = """
            INSERT INTO attendance
                (id_activity, id_member, is_present, is_excused, absence_reason)
            VALUES (?, ?, ?, ?, ?)
            RETURNING *
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendance.getActivityId());
            stmt.setInt(2, attendance.getMemberId());
            stmt.setBoolean(3, Boolean.TRUE.equals(attendance.getIsPresent()));
            stmt.setBoolean(4, Boolean.TRUE.equals(attendance.getIsExcused()));
            stmt.setString(5, attendance.getAbsenceReason());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return attendance;
        }
    }

    public boolean existsByActivityAndMember(int activityId, int memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE id_activity = ? AND id_member = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, activityId);
            stmt.setInt(2, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        }
    }

    public List<Attendance> findByActivityId(int activityId) throws SQLException {
        String sql = """
            SELECT a.*, m.last_name, m.first_name
            FROM attendance a
            JOIN member m ON m.id_member = a.id_member
            WHERE a.id_activity = ?
            ORDER BY m.last_name, m.first_name
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, activityId);
            ResultSet rs = stmt.executeQuery();
            List<Attendance> list = new ArrayList<>();
            while (rs.next()) {
                Attendance att = mapRow(rs);
                att.setMemberName(rs.getString("last_name") + " " + rs.getString("first_name"));
                list.add(att);
            }
            return list;
        }
    }

    private Attendance mapRow(ResultSet rs) throws SQLException {
        Attendance a = new Attendance();
        a.setIdAttendance(rs.getInt("id_attendance"));
        a.setActivityId(rs.getInt("id_activity"));
        a.setMemberId(rs.getInt("id_member"));
        a.setIsPresent(rs.getBoolean("is_present"));
        a.setIsExcused(rs.getBoolean("is_excused"));
        a.setAbsenceReason(rs.getString("absence_reason"));
        return a;
    }
}