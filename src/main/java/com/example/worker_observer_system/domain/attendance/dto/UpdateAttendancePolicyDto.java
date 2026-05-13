package com.example.worker_observer_system.domain.attendance.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAttendancePolicyDto {
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private LocalTime startTime;
    private LocalTime endTime;
}