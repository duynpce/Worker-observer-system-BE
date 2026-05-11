package com.example.worker_observer_system.domain.attendance.service.query;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import com.example.worker_observer_system.domain.attendance.dto.DailyAttendanceFilter;
import com.example.worker_observer_system.domain.attendance.entity.DailyAttendance;
import com.example.worker_observer_system.domain.attendance.repo.DailyAttendanceRepository;
import com.example.worker_observer_system.domain.attendance.specification.DailyAttendanceSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class DailyAttendanceQueryService {
    private final DailyAttendanceRepository dailyAttendanceRepository;

    @Transactional(readOnly = true)
    public List<DailyAttendance> findByFilter(DailyAttendanceFilter filter) {
        Specification<DailyAttendance> spec = DailyAttendanceSpecification.withFilter(filter);
        return dailyAttendanceRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Optional<DailyAttendance> findByAccountIdAndDateAndType(UUID accountId, LocalDate date, AttendanceType type) {
        return dailyAttendanceRepository.findByAccount_IdAndAttendanceDateAndType(accountId, date, type);
    }

    @Transactional(readOnly = true)
    public boolean existsByAccountIdAndDateAndType(UUID accountId, LocalDate date, AttendanceType type) {
        return dailyAttendanceRepository.existsByAccount_IdAndAttendanceDateAndType(accountId, date, type);
    }

    @Transactional
    public DailyAttendance save(DailyAttendance dailyAttendance) {
        return dailyAttendanceRepository.save(dailyAttendance);
    }
}