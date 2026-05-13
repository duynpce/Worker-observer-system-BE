package com.example.worker_observer_system.domain.quantity_report.mapper;

import com.example.worker_observer_system.domain.quantity_report.QuantityReport;
import com.example.worker_observer_system.domain.quantity_report.dto.GetQuantityReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuantityReportMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountName", source = "account.name")
    @Mapping(target = "accountEmail", source = "account.email")
    GetQuantityReportDto toDto(QuantityReport quantityReport);

    List<GetQuantityReportDto> toDtoList(List<QuantityReport> quantityReports);
}

