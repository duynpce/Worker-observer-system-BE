package com.example.worker_observer_system.domain.attendance.service.domain;
import com.example.worker_observer_system.domain.attendance.dto.CreateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.dto.UpdateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import com.example.worker_observer_system.domain.attendance.mapper.AttendancePolicyMapper;
import com.example.worker_observer_system.domain.attendance.service.query.AttendancePolicyQueryService;
import com.example.worker_observer_system.domain.attendance.validator.AttendancePolicyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AttendancePolicyDomainService {
    private final AttendancePolicyMapper attendancePolicyMapper;
    private final AttendancePolicyQueryService attendancePolicyQueryService;
    private final AttendancePolicyValidator attendancePolicyValidator;

    @Transactional
    public AttendancePolicy create(CreateAttendancePolicyDto dto) {
        AttendancePolicy policy = attendancePolicyMapper.toEntity(dto);
        attendancePolicyValidator.validateCreate(policy);
        return attendancePolicyQueryService.save(policy);
    }

    @Transactional
    public AttendancePolicy update(Long id, UpdateAttendancePolicyDto dto) {
        AttendancePolicy existingPolicy = attendancePolicyQueryService.findById(id);
        attendancePolicyValidator.validateUpdate(dto, existingPolicy);
        return attendancePolicyQueryService.save(existingPolicy);
    }
}