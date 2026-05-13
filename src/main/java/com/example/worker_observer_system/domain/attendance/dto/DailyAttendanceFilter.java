package com.example.worker_observer_system.domain.attendance.dto;
import com.example.worker_observer_system.common.dto.PaginationDto;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceStatus;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyAttendanceFilter {
    @NotNull(message = "attendanceDate is required")
    private LocalDate attendanceDate;
    private AttendanceType type;
    private AttendanceStatus status;

    @Valid
    @NotNull(message = "paginationDto is required")
    private PaginationDto paginationDto;
}