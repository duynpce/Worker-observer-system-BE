package com.example.worker_observer_system.domain.quantity_report.specification;

import com.example.worker_observer_system.domain.quantity_report.QuantityReport;
import com.example.worker_observer_system.domain.quantity_report.dto.QuantityReportFilter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class QuantityReportSpecification {

    private QuantityReportSpecification() {}

    public static Specification<QuantityReport> withFilter(QuantityReportFilter filter) {
        return Specification.allOf(hasDate(filter.getDate()));
    }

    private static Specification<QuantityReport> hasDate(LocalDate date) {
        return (root, query, cb) -> cb.equal(root.get("date"), date);
    }
}

