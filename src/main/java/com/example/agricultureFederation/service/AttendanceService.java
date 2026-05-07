package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.request.CreateAttendanceRequest;
import com.example.agricultureFederation.dto.response.AttendanceResponse;
import com.example.agricultureFederation.entity.Activity;
import com.example.agricultureFederation.entity.Attendance;
import com.example.agricultureFederation.entity.Member;
import com.example.agricultureFederation.repository.ActivityRepository;
import com.example.agricultureFederation.repository.AttendanceRepository;
import com.example.agricultureFederation.repository.CollectiveRepository;
import com.example.agricultureFederation.repository.MemberRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final CollectiveRepository collectiveRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             ActivityRepository activityRepository,
                             MemberRepository memberRepository,
                             CollectiveRepository collectiveRepository) {
        this.attendanceRepository = attendanceRepository;
        this.activityRepository = activityRepository;
        this.memberRepository = memberRepository;
        this.collectiveRepository = collectiveRepository;
    }

    public List<AttendanceResponse> createAttendances(String collectiveId,
                                                      String activityId,
                                                      List<CreateAttendanceRequest> requests) throws SQLException {
        int colId = Integer.parseInt(collectiveId);
        int actId = Integer.parseInt(activityId);

        if (collectiveRepository.findById(colId) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }

        Activity activity = activityRepository.findById(actId);
        if (activity == null) {
            throw new IllegalArgumentException("Activity not found: " + activityId);
        }

        if (activity.getCollectiveId() == null || activity.getCollectiveId() != colId) {
            throw new IllegalArgumentException("Activity does not belong to collectivity: " + collectiveId);
        }

        List<AttendanceResponse> responses = new ArrayList<>();
        for (CreateAttendanceRequest request : requests) {
            int memberId = request.getMemberId();

            Member member = memberRepository.findById(memberId);
            if (member == null) {
                throw new IllegalArgumentException("Member not found: " + memberId);
            }

            if (attendanceRepository.existsByActivityAndMember(actId, memberId)) {
                throw new IllegalStateException(
                        "Attendance already recorded for member " + memberId +
                                " at activity " + actId + ". Cannot be modified."
                );
            }

            Attendance attendance = new Attendance();
            attendance.setActivityId(actId);
            attendance.setMemberId(memberId);
            attendance.setIsPresent(Boolean.TRUE.equals(request.getIsPresent()));
            attendance.setIsExcused(Boolean.TRUE.equals(request.getIsExcused()));
            attendance.setAbsenceReason(request.getAbsenceReason());

            Attendance saved = attendanceRepository.save(attendance);
            responses.add(toResponse(saved, member));
        }
        return responses;
    }

    public List<AttendanceResponse> getAttendances(String collectiveId,
                                                   String activityId) throws SQLException {
        int colId = Integer.parseInt(collectiveId);
        int actId = Integer.parseInt(activityId);

        if (collectiveRepository.findById(colId) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }

        Activity activity = activityRepository.findById(actId);
        if (activity == null) {
            throw new IllegalArgumentException("Activity not found: " + activityId);
        }

        List<Attendance> attendances = attendanceRepository.findByActivityId(actId);
        List<AttendanceResponse> responses = new ArrayList<>();
        for (Attendance a : attendances) {
            Member member = memberRepository.findById(a.getMemberId());
            responses.add(toResponse(a, member));
        }
        return responses;
    }

    private AttendanceResponse toResponse(Attendance a, Member member) {
        AttendanceResponse r = new AttendanceResponse();
        r.setId(a.getIdAttendance());
        r.setActivityId(a.getActivityId());
        r.setMemberId(a.getMemberId());
        if (member != null) {
            r.setMemberFullName(member.getFirstName() + " " + member.getLastName());
            r.setMemberCollectiveId(member.getCollectiveId());
        }
        r.setIsPresent(a.getIsPresent());
        r.setIsExcused(a.getIsExcused());
        r.setAbsenceReason(a.getAbsenceReason());
        return r;
    }
}