package com.louter.collab.domain.schedule.dto.response;

import com.louter.collab.domain.schedule.entity.Color;
import com.louter.collab.domain.schedule.entity.Schedule;
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
    private String userName;

    public static ScheduleResponse from(Schedule schedule, String userName) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .teamId(schedule.getTeam().getTeamId())
                .scheduleTitle(schedule.getScheduleTitle())
                .scheduleContent(schedule.getScheduleContent())
                .scheduleDate(schedule.getScheduleDate())
                .color(schedule.getScheduleColor())
                .userName(userName)
                .build();
    }

    public static ScheduleResponse from(Schedule schedule) {
        return from(schedule, null);
    }
}
