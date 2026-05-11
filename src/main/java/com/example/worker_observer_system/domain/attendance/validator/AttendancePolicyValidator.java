package com.example.worker_observer_system.domain.attendance.validator;
import com.example.worker_observer_system.common.exception.ValidationException;
import com.example.worker_observer_system.domain.attendance.dto.UpdateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import com.example.worker_observer_system.domain.attendance.repo.AttendancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
@Component
@RequiredArgsConstructor
public class AttendancePolicyValidator {
    private final AttendancePolicyRepository attendancePolicyRepository;

    public void validateCreate(AttendancePolicy policy) {
        validateDateRange(policy.getEffectiveFrom(), policy.getEffectiveTo());
        if (attendancePolicyRepository.hasOverlap(policy.getEffectiveFrom(), policy.getEffectiveTo())) {
            throw new ValidationException("An overlapping attendance policy already exists");
        }
    }

    public void validateUpdate(UpdateAttendancePolicyDto request, AttendancePolicy existingPolicy) {
        if (isAllFieldsNull(request)) {
            throw new ValidationException("At least one field must be provided for update");
        }

        setNonNullFields(request, existingPolicy);
        validateDateRange(existingPolicy.getEffectiveFrom(), existingPolicy.getEffectiveTo());
        if (attendancePolicyRepository.hasOverlapExcludingSelf(
                existingPolicy.getId(), existingPolicy.getEffectiveFrom(), existingPolicy.getEffectiveTo())) {
            throw new ValidationException("Updating this policy would cause an overlap with an existing policy");
        }
    }

    private boolean isAllFieldsNull(UpdateAttendancePolicyDto request) {
        return request.getEffectiveFrom() == null
                && request.getEffectiveTo() == null
                && request.getStartTime() == null
                && request.getEndTime() == null;
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new ValidationException("effectiveFrom and effectiveTo must not be null");
        }
        if (from.isAfter(to)) {
            throw new ValidationException("effectiveFrom must not be after effectiveTo");
        }
    }

    private void setNonNullFields(UpdateAttendancePolicyDto request, AttendancePolicy policy) {
        if (request.getEffectiveFrom() != null) policy.setEffectiveFrom(request.getEffectiveFrom());
        if (request.getEffectiveTo() != null) policy.setEffectiveTo(request.getEffectiveTo());
        if (request.getStartTime() != null) policy.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) policy.setEndTime(request.getEndTime());
    }
}