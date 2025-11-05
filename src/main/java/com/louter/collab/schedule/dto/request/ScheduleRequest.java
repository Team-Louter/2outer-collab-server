package com.louter.collab.schedule.dto.request;

import com.louter.collab.schedule.domain.Color;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
