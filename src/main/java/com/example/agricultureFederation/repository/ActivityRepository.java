package com.example.agricultureFederation.repository;

import com.example.agricultureFederation.entity.Activity;
import com.example.agricultureFederation.entity.enums.ActivityTypeType;
import com.example.agricultureFederation.entity.enums.TargetMembersType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityRepository {

    private final DataSource dataSource;

    public ActivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Activity save(Activity activity) throws SQLException {
        String sql = """
            INSERT INTO activity
                (id_collective, id_federation, title, activity_type,
                 activity_date, attendance_required, target_members, is_federation)
            VALUES (?, ?, ?, ?::activity_type_type, ?, ?, ?::target_members_type, ?)
            RETURNING *
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, activity.getCollectiveId());
            stmt.setObject(2, activity.getFederationId());
            stmt.setString(3, activity.getTitle());
            stmt.setString(4, activity.getActivityType().name());
            stmt.setDate(5, Date.valueOf(activity.getActivityDate()));
            stmt.setBoolean(6, activity.getAttendanceRequired() != null && activity.getAttendanceRequired());
            stmt.setString(7, activity.getTargetMembers() != null
                    ? activity.getTargetMembers().name()
                    : TargetMembersType.All.name());
            stmt.setBoolean(8, activity.getIsFederation() != null && activity.getIsFederation());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return activity;
        }
    }

    public Activity findById(int activityId) throws SQLException {
        String sql = "SELECT * FROM activity WHERE id_activity = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, activityId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    public List<Activity> findByCollectiveId(int collectiveId) throws SQLException {
        String sql = "SELECT * FROM activity WHERE id_collective = ? ORDER BY activity_date DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, collectiveId);
            ResultSet rs = stmt.executeQuery();
            List<Activity> list = new ArrayList<>();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        }
    }

    private Activity mapRow(ResultSet rs) throws SQLException {
        Activity a = new Activity();
        a.setIdActivity(rs.getInt("id_activity"));
        a.setCollectiveId((Integer) rs.getObject("id_collective"));
        a.setFederationId((Integer) rs.getObject("id_federation"));
        a.setTitle(rs.getString("title"));
        try {
            a.setActivityType(ActivityTypeType.valueOf(rs.getString("activity_type")));
        } catch (Exception ignored) {}
        a.setActivityDate(rs.getDate("activity_date").toLocalDate());
        a.setAttendanceRequired(rs.getBoolean("attendance_required"));
        try {
            a.setTargetMembers(TargetMembersType.valueOf(rs.getString("target_members")));
        } catch (Exception ignored) {}
        a.setIsFederation(rs.getBoolean("is_federation"));
        return a;
    }
}