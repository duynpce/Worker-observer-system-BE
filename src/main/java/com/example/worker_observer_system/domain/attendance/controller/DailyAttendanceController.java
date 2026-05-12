package com.example.worker_observer_system.domain.attendance.controller;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import com.example.worker_observer_system.domain.attendance.dto.DailyAttendanceFilter;
import com.example.worker_observer_system.domain.attendance.dto.GetDailyAttendanceDto;
import com.example.worker_observer_system.domain.attendance.service.domain.DailyAttendanceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/v1/daily-attendance")
@RequiredArgsConstructor
public class DailyAttendanceController {
    private final DailyAttendanceDomainService dailyAttendanceDomainService;

    @PostMapping("/check-attendance")
    public ResponseEntity<ResponseDto<Void>> checkAttendance(
            @RequestParam AttendanceType attendanceType
    ) {
        dailyAttendanceDomainService.checkAttendance(attendanceType);
        String message = attendanceType == AttendanceType.START_TIME ? "Check-in successful" : "Check-out successful";
        return ResponseEntity.ok(ResponseDto.success(null, message));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetDailyAttendanceDto>>> getByFilter(
          @ModelAttribute  DailyAttendanceFilter filter
    ) {
        ResponseDto<List<GetDailyAttendanceDto>> response = dailyAttendanceDomainService.getByFilter(filter);
        return ResponseEntity.ok(response);
    }
}