package com.example.worker_observer_system.domain.quantity_report.controller;

import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.domain.quantity_report.dto.CreateQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.GetQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.QuantityReportFilter;
import com.example.worker_observer_system.domain.quantity_report.service.domain.QuantityReportDomainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/quantity-report")
@RequiredArgsConstructor
public class QuantityReportController {

    private final QuantityReportDomainService quantityReportDomainService;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> create(@Valid @RequestBody CreateQuantityReportDto dto) {
        quantityReportDomainService.create(dto);
        return ResponseEntity.ok(ResponseDto.success(null, "Quantity report created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetQuantityReportDto>>> getByFilter(
            @ModelAttribute QuantityReportFilter filter
    ) {
        ResponseDto<List<GetQuantityReportDto>> response = quantityReportDomainService.getByFilter(filter);
        return ResponseEntity.ok(response);
    }
}

