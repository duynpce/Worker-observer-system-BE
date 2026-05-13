package com.example.worker_observer_system.domain.attendance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttendancePolicyDto {

    @NotNull(message = "effectiveFrom is required")
    private LocalDate effectiveFrom;

    @NotNull(message = "effectiveTo is required")
    private LocalDate effectiveTo;

    @NotNull(message = "startTime is required")
    private LocalTime startTime;

    @NotNull(message = "endTime is required")
    private LocalTime endTime;
}

