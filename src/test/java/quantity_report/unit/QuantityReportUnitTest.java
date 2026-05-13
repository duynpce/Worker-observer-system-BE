package quantity_report.unit;

import com.example.worker_observer_system.common.UnitTest;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.common.util.JwtUtil;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import com.example.worker_observer_system.domain.quantity_report.QuantityReport;
import com.example.worker_observer_system.domain.quantity_report.dto.CreateQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.GetQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.QuantityReportFilter;
import com.example.worker_observer_system.domain.quantity_report.mapper.QuantityReportMapper;
import com.example.worker_observer_system.domain.quantity_report.service.domain.QuantityReportDomainService;
import com.example.worker_observer_system.domain.quantity_report.service.query.QuantityReportQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuantityReportUnitTest extends UnitTest {

    @Mock
    private QuantityReportQueryService quantityReportQueryService;
    @Mock
    private QuantityReportMapper quantityReportMapper;
    @Mock
    private AccountQueryService accountQueryService;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private QuantityReportDomainService quantityReportDomainService;

    private quantity_report.QuantityReportTestCases quantityReportTestCases;
    private account.AccountTestCases accountTestCases;

    @BeforeEach
    void setUp() {
        quantityReportTestCases = quantity_report.QuantityReportTestCases.getInstance();
        accountTestCases = account.AccountTestCases.getInstance();
    }

    @Test
    void createQuantityReportSuccess_newRecord() {
        Account account = accountTestCases.getAccountTestCase();
        CreateQuantityReportDto dto = quantityReportTestCases.getCreateQuantityReportDto();

        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
        when(accountQueryService.findById(account.getId())).thenReturn(account);
        when(quantityReportQueryService.findByAccountIdAndDate(any(UUID.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(quantityReportQueryService.save(any(QuantityReport.class))).thenAnswer(inv -> inv.getArgument(0));

        QuantityReport result = quantityReportDomainService.create(dto);

        assertNotNull(result);
        assertEquals(dto.getQuantity(), result.getQuantity());
        assertEquals(account, result.getAccount());
        verify(quantityReportQueryService).save(any(QuantityReport.class));
    }

    @Test
    void createQuantityReportSuccess_overrideExisting() {
        Account account = accountTestCases.getAccountTestCase();
        CreateQuantityReportDto dto = quantityReportTestCases.getUpdateQuantityReportDto();
        QuantityReport existing = quantityReportTestCases.getQuantityReportTestCase();
        existing.setAccount(account);

        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
        when(accountQueryService.findById(account.getId())).thenReturn(account);
        when(quantityReportQueryService.findByAccountIdAndDate(any(UUID.class), any(LocalDate.class)))
                .thenReturn(Optional.of(existing));
        when(quantityReportQueryService.save(existing)).thenReturn(existing);

        QuantityReport result = quantityReportDomainService.create(dto);

        assertNotNull(result);
        assertEquals(dto.getQuantity(), result.getQuantity());
        assertEquals(existing.getId(), result.getId());
        verify(quantityReportQueryService).save(existing);
    }

    @Test
    void createQuantityReportFail_accountNotFound() {
        Account account = accountTestCases.getAccountTestCase();
        CreateQuantityReportDto dto = quantityReportTestCases.getCreateQuantityReportDto();

        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
        when(accountQueryService.findById(account.getId()))
                .thenThrow(new NotFoundException("Account not found with id: " + account.getId()));

        assertThrows(NotFoundException.class, () -> quantityReportDomainService.create(dto));
        verify(quantityReportQueryService, never()).save(any());
    }

    @Test
    void getByFilterSuccess() {
        Account account = accountTestCases.getAccountTestCase();
        QuantityReport report = quantityReportTestCases.getQuantityReportTestCase();
        report.setAccount(account);
        QuantityReportFilter filter = quantityReportTestCases.getQuantityReportFilter();
        GetQuantityReportDto reportDto = new GetQuantityReportDto(report.getId(), report.getQuantity(), report.getDate(),
                account.getId(), account.getName(), account.getEmail());

        when(quantityReportQueryService.findByFilter(filter)).thenReturn(List.of(report));
        when(accountQueryService.findAll()).thenReturn(List.of(account));
        when(quantityReportMapper.toDtoList(List.of(report))).thenReturn(List.of(reportDto));

        ResponseDto<List<GetQuantityReportDto>> response = quantityReportDomainService.getByFilter(filter);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Quantity reports retrieved successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertNotNull(response.getMetaData());
        assertEquals(1, response.getMetaData().getTotalItems());
    }

    @Test
    void getByFilterSuccess_includesAbsentAccounts() {
        Account account = accountTestCases.getAccountTestCase();
        QuantityReportFilter filter = quantityReportTestCases.getQuantityReportFilter();

        when(quantityReportQueryService.findByFilter(filter)).thenReturn(List.of());
        when(accountQueryService.findAll()).thenReturn(List.of(account));
        when(quantityReportMapper.toDtoList(List.of())).thenReturn(List.of());

        ResponseDto<List<GetQuantityReportDto>> response = quantityReportDomainService.getByFilter(filter);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size(), "Absent account should be included");
        assertEquals(0, response.getData().getFirst().getQuantity(), "Absent account quantity should be 0");
        assertEquals(account.getId(), response.getData().getFirst().getAccountId());
    }
}

