package com.example.worker_observer_system.domain.attendance.repo;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import com.example.worker_observer_system.domain.attendance.entity.DailyAttendance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
public interface DailyAttendanceRepository extends JpaRepository<DailyAttendance, Integer>,
        JpaSpecificationExecutor<DailyAttendance> {
    Optional<DailyAttendance> findByAccount_IdAndAttendanceDateAndType(
            UUID accountId, LocalDate date, AttendanceType type);

    boolean existsByAccount_IdAndAttendanceDateAndType(
            UUID accountId, LocalDate date, AttendanceType type);
}