package com.louter.collab.schedule.dto.request;

import com.louter.collab.schedule.domain.Color;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleRequest {
    @NotNull
    private Long userId;

    @NotNull
    private String scheduleTitle;

    @NotNull
    private String scheduleContent;

    @NotNull
    private LocalDate scheduleDate;

    @NotNull
    private Color color;
}
