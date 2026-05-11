package com.example.worker_observer_system.domain.attendance.controller;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.domain.attendance.dto.CreateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.dto.UpdateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.service.domain.AttendancePolicyDomainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/v1/attendance-policy")
@RequiredArgsConstructor
public class AttendancePolicyController {
    private final AttendancePolicyDomainService attendancePolicyDomainService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateAttendancePolicyDto dto) {
        attendancePolicyDomainService.create(dto);
        return ResponseEntity.ok(ResponseDto.success(null, "Attendance policy created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAttendancePolicyDto dto
    ) {
        attendancePolicyDomainService.update(id, dto);
        return ResponseEntity.ok(ResponseDto.success(null, "Attendance policy updated successfully"));
    }
}