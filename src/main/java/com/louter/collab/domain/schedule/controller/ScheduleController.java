package com.louter.collab.domain.schedule.controller;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.global.common.exception.ScheduleNotFoundException;
import com.louter.collab.domain.schedule.entity.Schedule;
import com.louter.collab.domain.schedule.dto.request.ScheduleRequest;
import com.louter.collab.domain.schedule.dto.response.ScheduleResponse;
import com.louter.collab.domain.schedule.repository.ScheduleRepository;
import com.louter.collab.domain.team.entity.Team;
import com.louter.collab.domain.team.repository.TeamRepository;
import com.louter.collab.global.common.exception.TeamNotFoundException;
import com.louter.collab.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/schedule")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    // 일정 생성
    @PostMapping
    public ResponseEntity<Object> createSchedule(
            @PathVariable("teamId") Long teamId,
            @RequestBody ScheduleRequest scheduleRequest, Long userId) {
        try {
            Team team = teamRepository.findByTeamId(teamId)
                    .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

            Schedule schedule = Schedule.builder()
                    .team(team)
                    .user(user)
                    .scheduleTitle(scheduleRequest.getScheduleTitle())
                    .scheduleContent(scheduleRequest.getScheduleContent())
                    .scheduleDate(scheduleRequest.getScheduleDate())
                    .scheduleColor(scheduleRequest.getColor())
                    .build();

            Schedule saved = scheduleRepository.save(schedule);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "schedule", ScheduleResponse.from(saved, saved.getUser().getUserName())));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 모든 일정 조회
    @GetMapping
    public ResponseEntity<Object> getSchedules(@PathVariable("teamId") Long teamId, String userName) {
        try {
            Team team = teamRepository.findByTeamId(teamId)
                    .orElseThrow(() -> new TeamNotFoundException("팀을 찾을 수 없습니다"));

            List<Schedule> schedules = scheduleRepository.findByTeam(team);
            List<ScheduleResponse> response = schedules.stream()
                    .map(ScheduleResponse::from)
                    .toList();

            return ResponseEntity.ok(Map.of("success", true, "schedules", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 특정 일정 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Object> getSchedule(@PathVariable("teamId") Long teamId,
                                              @PathVariable("scheduleId") Long scheduleId) {
        try {
            Schedule schedule = scheduleRepository.findByScheduleIdAndTeamTeamId(scheduleId, teamId)
                    .orElseThrow(() -> new ScheduleNotFoundException("일정을 찾을 수 없습니다."));

            return ResponseEntity.ok(Map.of("success", true, "schedule", ScheduleResponse.from(schedule)));
        } catch(ScheduleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 일정 수정
    @PutMapping("/{scheduleId}")
    public ResponseEntity<Object> updateSchedule(@PathVariable("teamId") Long teamId,
                                                 @PathVariable("scheduleId") Long scheduleId,
                                                 @RequestBody ScheduleRequest scheduleRequest) {
        try{
            Schedule schedule = scheduleRepository.findByScheduleIdAndTeamTeamId(scheduleId, teamId)
                    .orElseThrow(() -> new ScheduleNotFoundException("일정을 찾을 수 없습니다."));

            schedule.setScheduleTitle(scheduleRequest.getScheduleTitle());
            schedule.setScheduleContent(scheduleRequest.getScheduleContent());
            schedule.setScheduleDate(scheduleRequest.getScheduleDate());
            schedule.setScheduleColor(scheduleRequest.getColor());

            Schedule updated = scheduleRepository.save(schedule);

            return ResponseEntity.ok(Map.of("success", true, "schedule", ScheduleResponse.from(updated)));
        } catch(ScheduleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 일정 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Object> deleteSchedule(@PathVariable("teamId") Long teamId,
                                                 @PathVariable("scheduleId") Long scheduleId) {
        try{
            Schedule schedule = scheduleRepository.findByScheduleIdAndTeamTeamId(scheduleId, teamId)
                    .orElseThrow(() -> new ScheduleNotFoundException("일정을 찾을 수 없습니다."));

            scheduleRepository.delete(schedule);

            return ResponseEntity.ok(Map.of("success", true, "message", "삭제 완료"));
        } catch(ScheduleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}