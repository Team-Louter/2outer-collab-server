package com.louter.collab.domain.schedule.repository;

import com.louter.collab.domain.schedule.domain.Schedule;
import com.louter.collab.domain.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // user의 모든 일정 조회
    List<Schedule> findByTeam(Team teamId);

    // user의 특정 일정 조회
    Optional<Schedule> findByScheduleIdAndTeamTeamId(Long scheduleId, Long teamId);
}
