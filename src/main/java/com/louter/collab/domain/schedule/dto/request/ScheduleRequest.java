package com.louter.collab.domain.schedule.dto.request;

import com.louter.collab.domain.schedule.entity.Color;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequest {
    @NotNull
    private String scheduleTitle;

    @NotNull
    private String scheduleContent;

    @NotNull
    private LocalDateTime scheduleDate;

    @NotNull
    private Color color;
}
