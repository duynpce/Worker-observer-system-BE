package quantity_report;

import com.example.worker_observer_system.common.dto.PaginationDto;
import com.example.worker_observer_system.domain.quantity_report.QuantityReport;
import com.example.worker_observer_system.domain.quantity_report.dto.CreateQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.QuantityReportFilter;

import java.time.LocalDate;

public class QuantityReportTestCases {

    private static QuantityReportTestCases instance;

    public static QuantityReportTestCases getInstance() {
        if (instance == null) {
            instance = new QuantityReportTestCases();
        }
        return instance;
    }

    public QuantityReport getQuantityReportTestCase() {
        QuantityReport report = new QuantityReport();
        report.setId(1L);
        report.setQuantity(50);
        report.setDate(LocalDate.now());
        return report;
    }

    public CreateQuantityReportDto getCreateQuantityReportDto() {
        CreateQuantityReportDto dto = new CreateQuantityReportDto();
        dto.setQuantity(50);
        return dto;
    }

    public CreateQuantityReportDto getUpdateQuantityReportDto() {
        CreateQuantityReportDto dto = new CreateQuantityReportDto();
        dto.setQuantity(100);
        return dto;
    }

    public QuantityReportFilter getQuantityReportFilter() {
        QuantityReportFilter filter = new QuantityReportFilter();
        filter.setDate(LocalDate.now());
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);
        filter.setPaginationDto(paginationDto);
        return filter;
    }

    public QuantityReportFilter getOutOfRangeQuantityReportFilter() {
        QuantityReportFilter filter = new QuantityReportFilter();
        filter.setDate(LocalDate.now());
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(1000);
        paginationDto.setLimit(10);
        filter.setPaginationDto(paginationDto);
        return filter;
    }
}

