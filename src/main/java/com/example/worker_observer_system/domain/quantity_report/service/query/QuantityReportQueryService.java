package com.example.worker_observer_system.domain.quantity_report.service.query;

import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.domain.quantity_report.QuantityReport;
import com.example.worker_observer_system.domain.quantity_report.dto.QuantityReportFilter;
import com.example.worker_observer_system.domain.quantity_report.repo.QuantityReportRepository;
import com.example.worker_observer_system.domain.quantity_report.specification.QuantityReportSpecification;
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
public class QuantityReportQueryService {

    private final QuantityReportRepository quantityReportRepository;

    @Transactional(readOnly = true)
    public List<QuantityReport> findByFilter(QuantityReportFilter filter) {
        Specification<QuantityReport> spec = QuantityReportSpecification.withFilter(filter);
        return quantityReportRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Optional<QuantityReport> findByAccountIdAndDate(UUID accountId, LocalDate date) {
        return quantityReportRepository.findByAccount_IdAndDate(accountId, date);
    }

    @Transactional(readOnly = true)
    public QuantityReport findById(Long id) {
        return quantityReportRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Quantity report not found with id: " + id)
        );
    }

    @Transactional
    public QuantityReport save(QuantityReport quantityReport) {
        return quantityReportRepository.save(quantityReport);
    }
}

