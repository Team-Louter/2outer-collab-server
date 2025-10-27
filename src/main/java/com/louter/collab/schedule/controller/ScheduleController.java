package com.louter.collab.schedule.controller;

import com.louter.collab.auth.domain.User;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.common.exception.ScheduleNotFoundException;
import com.louter.collab.schedule.domain.Schedule;
import com.louter.collab.schedule.dto.request.ScheduleRequest;
import com.louter.collab.schedule.dto.response.ScheduleResponse;
import com.louter.collab.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/{userId}/schedule")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 일정 생성
    @PostMapping
    public ResponseEntity<Object> createSchedule(
            @PathVariable Long userId,
            @RequestBody ScheduleRequest scheduleRequest) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Schedule schedule = Schedule.builder()
                    .user(user)
                    .scheduleTitle(scheduleRequest.getScheduleTitle())
                    .scheduleContent(scheduleRequest.getScheduleContent())
                    .scheduleDate(scheduleRequest.getScheduleDate())
                    .scheduleColor(scheduleRequest.getColor())
                    .build();

            Schedule saved = scheduleRepository.save(schedule);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "schedule", ScheduleResponse.from(saved)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 모든 일정 조회
    @GetMapping
    public ResponseEntity<Object> getSchedules(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            List<Schedule> schedules = scheduleRepository.findByUser(user);
            List<ScheduleResponse> response = schedules.stream()
                    .map(ScheduleResponse::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("success", true, "schedules", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 특정 일정 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Object> getSchedule(@PathVariable Long userId,
                                              @PathVariable Long scheduleId) {
        try {
            Schedule schedule = scheduleRepository.findByScheduleIdAndUserUserId(scheduleId, userId)
                    .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));

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
    public ResponseEntity<Object> updateSchedule(@PathVariable Long userId,
                                                 @PathVariable Long scheduleId,
                                                 @RequestBody ScheduleRequest scheduleRequest) {
        try{
            Schedule schedule = scheduleRepository.findByScheduleIdAndUserUserId(scheduleId, userId)
                    .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));

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
    public ResponseEntity<Object> deleteSchedule(@PathVariable Long userId,
                                                 @PathVariable Long scheduleId) {
        try{
            Schedule schedule = scheduleRepository.findByScheduleIdAndUserUserId(scheduleId, userId)
                    .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));

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