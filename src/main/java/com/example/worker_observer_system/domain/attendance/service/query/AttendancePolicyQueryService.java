package com.example.worker_observer_system.domain.attendance.service.query;
import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import com.example.worker_observer_system.domain.attendance.repo.AttendancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AttendancePolicyQueryService {
    private final AttendancePolicyRepository attendancePolicyRepository;

    @Transactional(readOnly = true)
    public AttendancePolicy findById(Long id) {
        return attendancePolicyRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Attendance policy not found with id: " + id)
        );
    }
    @Transactional(readOnly = true)
    public AttendancePolicy findActivePolicyForDate(LocalDate date) {
        return attendancePolicyRepository
                .findFirstByEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(date, date)
                .orElseThrow(() -> new NotFoundException("No active attendance policy found for date: " + date));
    }

    @Transactional
    public AttendancePolicy save(AttendancePolicy policy) {
        return attendancePolicyRepository.save(policy);
    }
}