package attendance.integration;

import account.AccountTestCases;
import attendance.AttendanceTestCases;
import com.example.worker_observer_system.common.IntegrationTest;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.exception.ConflictDataException;
import com.example.worker_observer_system.common.util.JwtUtil;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.AccountController;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import com.example.worker_observer_system.domain.account.dto.CreateAccountDto;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import com.example.worker_observer_system.domain.attendance.controller.AttendancePolicyController;
import com.example.worker_observer_system.domain.attendance.controller.DailyAttendanceController;
import com.example.worker_observer_system.domain.attendance.dto.CreateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.dto.DailyAttendanceFilter;
import com.example.worker_observer_system.domain.attendance.dto.GetDailyAttendanceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DailyAttendanceIntegrationTest extends IntegrationTest {

    @Autowired
    private DailyAttendanceController dailyAttendanceController;

    @Autowired
    private AttendancePolicyController attendancePolicyController;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountQueryService accountQueryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final AttendanceTestCases attendanceTestCases = AttendanceTestCases.getInstance();
    private final  AccountTestCases accountTestCases = AccountTestCases.getInstance();




    @Test
    void checkAttendanceSuccess() {
        setupScenario();

        ResponseEntity<ResponseDto<Void>> response = dailyAttendanceController.checkAttendance(AttendanceType.START_TIME);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Attendance checked successfully", response.getBody().getMessage(), "Success message should match");
    }

    @Test
    void checkAttendanceFail_alreadyCheckedIn() {
        setupScenario();

        dailyAttendanceController.checkAttendance(AttendanceType.START_TIME);

        assertThrows(ConflictDataException.class,
                () -> dailyAttendanceController.checkAttendance(AttendanceType.START_TIME),
                "Should throw ConflictDataException when checking in twice");
    }

    @Test
    void getByFilterSuccess() {
        setupScenario();
        dailyAttendanceController.checkAttendance(AttendanceType.START_TIME);

        DailyAttendanceFilter filter = attendanceTestCases.getDailyAttendanceFilter();
        ResponseEntity<ResponseDto<List<GetDailyAttendanceDto>>> response = dailyAttendanceController.getByFilter(filter);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Daily attendances retrieved successfully", response.getBody().getMessage(), "Success message should match");
        assertNotNull(response.getBody().getData(), "Response data should not be null");
        assertFalse(response.getBody().getData().isEmpty(), "Response data should not be empty");
        assertNotNull(response.getBody().getMetaData(), "Metadata should not be null");
        assertTrue(response.getBody().getMetaData().getTotalItems() > 0, "Total items should be greater than 0");
    }

    @Test
    void getByFilterEmpty_outOfRange() {
        setupScenario();
        dailyAttendanceController.checkAttendance(AttendanceType.START_TIME);

        DailyAttendanceFilter filter = attendanceTestCases.getOutOfRangeDailyAttendanceFilter();
        ResponseEntity<ResponseDto<List<GetDailyAttendanceDto>>> response = dailyAttendanceController.getByFilter(filter);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertTrue(response.getBody().getData().isEmpty(), "Response data should be empty for out-of-range page");
    }

    private void setupScenario() {
        CreateAccountDto createAccountDto = accountTestCases.getCreateAccountDto();
        accountController.create(createAccountDto);
        Account account = accountQueryService.findByEmail(createAccountDto.email());

        CreateAttendancePolicyDto createPolicyDto = attendanceTestCases.getCreateAttendancePolicyDto();
        attendancePolicyController.create(createPolicyDto);

        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
    }
}
