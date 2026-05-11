package com.example.worker_observer_system.domain.quantity_report.service.domain;

import com.example.worker_observer_system.common.dto.MetaDataDto;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.util.JwtUtil;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import com.example.worker_observer_system.domain.quantity_report.QuantityReport;
import com.example.worker_observer_system.domain.quantity_report.dto.CreateQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.GetQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.QuantityReportFilter;
import com.example.worker_observer_system.domain.quantity_report.mapper.QuantityReportMapper;
import com.example.worker_observer_system.domain.quantity_report.service.query.QuantityReportQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuantityReportDomainService {

    private final QuantityReportQueryService quantityReportQueryService;
    private final QuantityReportMapper quantityReportMapper;
    private final AccountQueryService accountQueryService;
    private final JwtUtil jwtUtil;

    @Transactional
    public QuantityReport create(CreateQuantityReportDto dto) {
        UUID accountId = UUID.fromString(jwtUtil.getUuid());
        Account account = accountQueryService.findById(accountId);
        LocalDate today = LocalDate.now();

        Optional<QuantityReport> existing = quantityReportQueryService.findByAccountIdAndDate(accountId, today);

        QuantityReport quantityReport = existing.orElseGet(QuantityReport::new);
        quantityReport.setQuantity(dto.getQuantity());
        quantityReport.setDate(today);
        quantityReport.setAccount(account);

        return quantityReportQueryService.save(quantityReport);
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<GetQuantityReportDto>> getByFilter(QuantityReportFilter filter) {
        List<QuantityReport> reports = quantityReportQueryService.findByFilter(filter);
        List<Account> allAccounts = accountQueryService.findAll();

        Set<UUID> reportedAccountIds = reports.stream()
                .map(r -> r.getAccount().getId())
                .collect(Collectors.toSet());

        List<GetQuantityReportDto> result = new ArrayList<>(quantityReportMapper.toDtoList(reports));
        allAccounts.stream()
                .filter(acc -> !reportedAccountIds.contains(acc.getId()))
                .map(acc -> buildAbsentDto(acc, filter))
                .forEach(result::add);

        int page = filter.getPaginationDto().getPage();
        int limit = filter.getPaginationDto().getLimit();
        int totalItems = result.size();
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int start = page * limit;
        int end = Math.min(start + limit, totalItems);
        List<GetQuantityReportDto> pagedResult = start >= totalItems ? Collections.emptyList() : result.subList(start, end);

        MetaDataDto metaData = MetaDataDto.builder()
                .totalItems(totalItems)
                .totalPages(totalPages)
                .paginationDto(filter.getPaginationDto())
                .build();

        return ResponseDto.success(pagedResult, "Quantity reports retrieved successfully", metaData);
    }

    private GetQuantityReportDto buildAbsentDto(Account account, QuantityReportFilter filter) {
        GetQuantityReportDto dto = new GetQuantityReportDto();
        dto.setAccountId(account.getId());
        dto.setAccountName(account.getName());
        dto.setAccountEmail(account.getEmail());
        dto.setDate(filter.getDate());
        dto.setQuantity(0);
        return dto;
    }
}

