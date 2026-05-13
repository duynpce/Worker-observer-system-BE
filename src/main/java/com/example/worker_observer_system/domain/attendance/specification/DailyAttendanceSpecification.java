package com.example.worker_observer_system.domain.attendance.specification;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceStatus;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import com.example.worker_observer_system.domain.attendance.dto.DailyAttendanceFilter;
import com.example.worker_observer_system.domain.attendance.entity.DailyAttendance;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
public class DailyAttendanceSpecification {
    private DailyAttendanceSpecification() {}
    public static Specification<DailyAttendance> withFilter(DailyAttendanceFilter filter) {
        return Specification
                .allOf(hasAttendanceDate(filter.getAttendanceDate()))
                .and(hasType(filter.getType()))
                .and(hasStatus(filter.getStatus()));
    }

    private static Specification<DailyAttendance> hasAttendanceDate(LocalDate date) {
        return (root, query, cb) -> cb.equal(root.get("attendanceDate"), date);
    }

    private static Specification<DailyAttendance> hasType(AttendanceType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
    }

    private static Specification<DailyAttendance> hasStatus(AttendanceStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }
}