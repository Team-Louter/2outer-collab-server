package com.louter.collab.domain.schedule.dto.response;

import com.louter.collab.domain.schedule.domain.Color;
import com.louter.collab.domain.schedule.domain.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ScheduleResponse {
    private Long teamId;
    private Long scheduleId;
    private String scheduleTitle;
    private String scheduleContent;
    private LocalDateTime scheduleDate;
    private Color color;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .teamId(schedule.getTeam().getTeamId())
                .scheduleTitle(schedule.getScheduleTitle())
                .scheduleContent(schedule.getScheduleContent())
                .scheduleDate(schedule.getScheduleDate())
                .color(schedule.getScheduleColor())
                .build();
    }
}
