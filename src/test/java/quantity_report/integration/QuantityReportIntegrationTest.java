package quantity_report.integration;

import account.AccountTestCases;
import com.example.worker_observer_system.common.IntegrationTest;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.util.JwtUtil;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.AccountController;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import com.example.worker_observer_system.domain.account.dto.CreateAccountDto;
import com.example.worker_observer_system.domain.quantity_report.controller.QuantityReportController;
import com.example.worker_observer_system.domain.quantity_report.dto.CreateQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.GetQuantityReportDto;
import com.example.worker_observer_system.domain.quantity_report.dto.QuantityReportFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import quantity_report.QuantityReportTestCases;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class QuantityReportIntegrationTest extends IntegrationTest {

    @Autowired
    private QuantityReportController quantityReportController;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountQueryService accountQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final QuantityReportTestCases quantityReportTestCases = QuantityReportTestCases.getInstance();
    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    @Test
    void createQuantityReportSuccess() {
        setupScenario();
        CreateQuantityReportDto dto = quantityReportTestCases.getCreateQuantityReportDto();

        ResponseEntity<ResponseDto<Void>> response = quantityReportController.create(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Quantity report created successfully", response.getBody().getMessage(), "Success message should match");
    }

    @Test
    void createQuantityReportSuccess_overrideExisting() {
        setupScenario();
        CreateQuantityReportDto createDto = quantityReportTestCases.getCreateQuantityReportDto();
        quantityReportController.create(createDto);

        CreateQuantityReportDto updateDto = quantityReportTestCases.getUpdateQuantityReportDto();
        ResponseEntity<ResponseDto<Void>> response = quantityReportController.create(updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Quantity report created successfully", response.getBody().getMessage(), "Success message should match");
    }

    @Test
    void getByFilterSuccess() {
        setupScenario();
        quantityReportController.create(quantityReportTestCases.getCreateQuantityReportDto());

        QuantityReportFilter filter = quantityReportTestCases.getQuantityReportFilter();
        ResponseEntity<ResponseDto<List<GetQuantityReportDto>>> response = quantityReportController.getByFilter(filter);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Quantity reports retrieved successfully", response.getBody().getMessage(), "Success message should match");
        assertNotNull(response.getBody().getData(), "Response data should not be null");
        assertFalse(response.getBody().getData().isEmpty(), "Response data should not be empty");
        assertNotNull(response.getBody().getMetaData(), "Metadata should not be null");
        assertTrue(response.getBody().getMetaData().getTotalItems() > 0, "Total items should be greater than 0");
    }

    @Test
    void getByFilterEmpty_outOfRange() {
        setupScenario();
        quantityReportController.create(quantityReportTestCases.getCreateQuantityReportDto());

        QuantityReportFilter filter = quantityReportTestCases.getOutOfRangeQuantityReportFilter();
        ResponseEntity<ResponseDto<List<GetQuantityReportDto>>> response = quantityReportController.getByFilter(filter);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertTrue(response.getBody().getData().isEmpty(), "Response data should be empty for out-of-range page");
    }

    private void
    setupScenario() {
        CreateAccountDto createAccountDto = accountTestCases.getCreateAccountDto();
        accountController.create(createAccountDto);
        Account account = accountQueryService.findByEmail(createAccountDto.email());
        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
    }
}

