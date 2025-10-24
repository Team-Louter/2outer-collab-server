package com.louter.collab.schedule.repository;

import com.louter.collab.auth.domain.User;
import com.louter.collab.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // user의 모든 일정 조회
    List<Schedule> findByUser(User userId);

    // user의 특정 일정 조회
    Optional<Schedule> findByScheduleIdAndUserUserId(Long scheduleId, Long userId);
}
