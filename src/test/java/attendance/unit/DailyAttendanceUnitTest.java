package attendance.unit;

import attendance.AttendanceTestCases;
import com.example.worker_observer_system.common.UnitTest;
import com.example.worker_observer_system.common.exception.ConflictDataException;
import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.common.util.JwtUtil;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceStatus;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import com.example.worker_observer_system.domain.attendance.entity.DailyAttendance;
import com.example.worker_observer_system.domain.attendance.mapper.DailyAttendanceMapper;
import com.example.worker_observer_system.domain.attendance.service.domain.DailyAttendanceDomainService;
import com.example.worker_observer_system.domain.attendance.service.query.AttendancePolicyQueryService;
import com.example.worker_observer_system.domain.attendance.service.query.DailyAttendanceQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DailyAttendanceUnitTest extends UnitTest {

    @Mock
    private DailyAttendanceQueryService dailyAttendanceQueryService;
    @Mock
    private DailyAttendanceMapper dailyAttendanceMapper;
    @Mock
    private AttendancePolicyQueryService attendancePolicyQueryService;
    @Mock
    private AccountQueryService accountQueryService;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private DailyAttendanceDomainService dailyAttendanceDomainService;

    private AttendanceTestCases attendanceTestCases;
    private account.AccountTestCases accountTestCases;

    @BeforeEach
    void setUp() {
        attendanceTestCases = AttendanceTestCases.getInstance();
        accountTestCases = account.AccountTestCases.getInstance();
    }

    @Test
    void checkAttendanceStartTimeSuccess() {
        Account account = accountTestCases.getAccountTestCase();
        AttendancePolicy policy = attendanceTestCases.getPolicyForOnTime();

        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
        when(accountQueryService.findById(account.getId())).thenReturn(account);
        when(attendancePolicyQueryService.findActivePolicyForDate(any(LocalDate.class))).thenReturn(policy);
        when(dailyAttendanceQueryService.existsByAccountIdAndDateAndType(any(UUID.class), any(LocalDate.class), eq(AttendanceType.START_TIME))).thenReturn(false);
        when(dailyAttendanceQueryService.save(any(DailyAttendance.class))).thenAnswer(inv -> inv.getArgument(0));

        DailyAttendance result = dailyAttendanceDomainService.checkAttendance(AttendanceType.START_TIME);

        assertNotNull(result);
        assertEquals(AttendanceType.START_TIME, result.getType());
        assertEquals(AttendanceStatus.ON_TIME, result.getStatus());
        verify(dailyAttendanceQueryService).save(any(DailyAttendance.class));
    }

    @Test
    void checkAttendanceStartTimeFail_alreadyCheckedIn() {
        Account account = accountTestCases.getAccountTestCase();
        AttendancePolicy policy = attendanceTestCases.getPolicyForOnTime();

        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
        when(accountQueryService.findById(account.getId())).thenReturn(account);
        when(attendancePolicyQueryService.findActivePolicyForDate(any(LocalDate.class))).thenReturn(policy);
        when(dailyAttendanceQueryService.existsByAccountIdAndDateAndType(any(UUID.class), any(LocalDate.class), eq(AttendanceType.START_TIME))).thenReturn(true);

        assertThrows(ConflictDataException.class, () -> dailyAttendanceDomainService.checkAttendance(AttendanceType.START_TIME));
        verify(dailyAttendanceQueryService, never()).save(any());
    }

    @Test
    void checkAttendanceEndTimeSuccess() {
        Account account = accountTestCases.getAccountTestCase();
        AttendancePolicy policy = attendanceTestCases.getPolicyForOnTime();

        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
        when(accountQueryService.findById(account.getId())).thenReturn(account);
        when(attendancePolicyQueryService.findActivePolicyForDate(any(LocalDate.class))).thenReturn(policy);
        when(dailyAttendanceQueryService.findByAccountIdAndDateAndType(any(UUID.class), any(LocalDate.class), eq(AttendanceType.START_TIME))).thenReturn(Optional.empty());
        when(dailyAttendanceQueryService.save(any(DailyAttendance.class))).thenAnswer(inv -> inv.getArgument(0));

        DailyAttendance result = dailyAttendanceDomainService.checkAttendance(AttendanceType.END_TIME);

        assertNotNull(result);
        assertEquals(AttendanceType.END_TIME, result.getType());
        assertEquals(AttendanceStatus.ON_TIME_LEAVE, result.getStatus());
        verify(dailyAttendanceQueryService).save(any(DailyAttendance.class));
    }

    @Test
    void checkAttendanceEndTimeFail_policyNotFound() {
        Account account = accountTestCases.getAccountTestCase();

        when(jwtUtil.getUuid()).thenReturn(account.getId().toString());
        when(accountQueryService.findById(account.getId())).thenReturn(account);
        when(attendancePolicyQueryService.findActivePolicyForDate(any(LocalDate.class)))
                .thenThrow(new NotFoundException("No active attendance policy found for date: " + LocalDate.now()));

        assertThrows(NotFoundException.class, () -> dailyAttendanceDomainService.checkAttendance(AttendanceType.END_TIME));
        verify(dailyAttendanceQueryService, never()).save(any());
    }
}
