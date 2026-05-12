package com.example.worker_observer_system.domain.quantity_report.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateQuantityReportDto {

    @NotNull(message = "quantity is required")
    private Integer quantity;
}

