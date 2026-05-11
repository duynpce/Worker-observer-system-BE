package com.example.worker_observer_system.domain.quantity_report.repo;

import com.example.worker_observer_system.domain.quantity_report.QuantityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface QuantityReportRepository extends JpaRepository<QuantityReport, Long>,
        JpaSpecificationExecutor<QuantityReport> {

    Optional<QuantityReport> findByAccount_IdAndDate(UUID accountId, LocalDate date);
}

