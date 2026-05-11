package com.example.worker_observer_system.domain.quantity_report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetQuantityReportDto {
    private Long id;
    private Integer quantity;
    private LocalDate date;
    private UUID accountId;
    private String accountName;
    private String accountEmail;
}

