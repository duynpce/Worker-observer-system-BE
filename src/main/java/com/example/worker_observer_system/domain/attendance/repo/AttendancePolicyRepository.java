package com.example.worker_observer_system.domain.attendance.repo;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
public interface AttendancePolicyRepository extends JpaRepository<AttendancePolicy, Long> {

    Optional<AttendancePolicy> findFirstByEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            LocalDate date1, LocalDate date2);

    boolean existsByEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            LocalDate effectiveTo, LocalDate effectiveFrom);

    boolean existsByIdNotAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            Long id, LocalDate effectiveTo, LocalDate effectiveFrom);

    default boolean hasOverlap(LocalDate effectiveFrom, LocalDate effectiveTo) {
        return existsByEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(effectiveTo, effectiveFrom);
    }

    default boolean hasOverlapExcludingSelf(Long id, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return existsByIdNotAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(id, effectiveTo, effectiveFrom);
    }
}