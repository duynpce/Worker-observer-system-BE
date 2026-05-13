package com.example.worker_observer_system.domain.quantity_report.dto;

import com.example.worker_observer_system.common.dto.PaginationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class QuantityReportFilter {

    @NotNull(message = "date is required")
    private LocalDate date;

    @Valid
    @NotNull(message = "paginationDto is required")
    private PaginationDto paginationDto;
}

