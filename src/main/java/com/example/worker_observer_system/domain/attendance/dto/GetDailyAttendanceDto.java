package com.example.worker_observer_system.domain.attendance.dto;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceStatus;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDailyAttendanceDto {
    private Integer id;
    private LocalDate attendanceDate;
    private LocalTime attendanceTime;
    private AttendanceStatus status;
    private AttendanceType type;
    private UUID accountId;
    private String accountName;
    private String accountEmail;
}