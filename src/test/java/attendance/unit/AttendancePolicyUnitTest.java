package attendance.unit;

import attendance.AttendanceTestCases;
import com.example.worker_observer_system.common.UnitTest;
import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.common.exception.ValidationException;
import com.example.worker_observer_system.domain.attendance.dto.CreateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.dto.UpdateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import com.example.worker_observer_system.domain.attendance.mapper.AttendancePolicyMapper;
import com.example.worker_observer_system.domain.attendance.service.domain.AttendancePolicyDomainService;
import com.example.worker_observer_system.domain.attendance.service.query.AttendancePolicyQueryService;
import com.example.worker_observer_system.domain.attendance.validator.AttendancePolicyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AttendancePolicyUnitTest extends UnitTest {

    @Mock
    private AttendancePolicyMapper attendancePolicyMapper;
    @Mock
    private AttendancePolicyQueryService attendancePolicyQueryService;
    @Mock
    private AttendancePolicyValidator attendancePolicyValidator;

    @InjectMocks
    private AttendancePolicyDomainService attendancePolicyDomainService;

    private AttendanceTestCases attendanceTestCases;

    @BeforeEach
    void setUp() {
        attendanceTestCases = AttendanceTestCases.getInstance();
    }

    @Test
    void createPolicySuccess() {
        CreateAttendancePolicyDto dto = attendanceTestCases.getCreateAttendancePolicyDto();
        AttendancePolicy policy = attendanceTestCases.getAttendancePolicyTestCase();

        when(attendancePolicyMapper.toEntity(dto)).thenReturn(policy);
        when(attendancePolicyQueryService.save(policy)).thenReturn(policy);

        AttendancePolicy result = attendancePolicyDomainService.create(dto);

        assertNotNull(result);
        assertEquals(dto.getEffectiveFrom(), result.getEffectiveFrom());
        verify(attendancePolicyValidator).validateCreate(policy);
        verify(attendancePolicyQueryService).save(policy);
    }

    @Test
    void createPolicyFail_overlap() {
        CreateAttendancePolicyDto dto = attendanceTestCases.getCreateAttendancePolicyDto();
        AttendancePolicy policy = attendanceTestCases.getAttendancePolicyTestCase();

        when(attendancePolicyMapper.toEntity(dto)).thenReturn(policy);
        doThrow(new ValidationException("An overlapping attendance policy already exists"))
                .when(attendancePolicyValidator).validateCreate(policy);

        assertThrows(ValidationException.class, () -> attendancePolicyDomainService.create(dto));
        verify(attendancePolicyQueryService, never()).save(any());
    }

    @Test
    void updatePolicySuccess() {
        Long id = attendanceTestCases.getAttendancePolicyTestCase().getId();
        UpdateAttendancePolicyDto dto = attendanceTestCases.getUpdateAttendancePolicyDto();
        AttendancePolicy existing = attendanceTestCases.getAttendancePolicyTestCase();

        when(attendancePolicyQueryService.findById(id)).thenReturn(existing);
        when(attendancePolicyQueryService.save(existing)).thenReturn(existing);

        AttendancePolicy result = attendancePolicyDomainService.update(id, dto);

        assertNotNull(result);
        assertEquals(existing.getEffectiveFrom(),result.getEffectiveFrom());
        verify(attendancePolicyValidator).validateUpdate(dto, existing);
        verify(attendancePolicyQueryService).save(existing);
    }

    @Test
    void updatePolicyFail_notFound() {
        Long id = 999L;
        UpdateAttendancePolicyDto dto = attendanceTestCases.getUpdateAttendancePolicyDto();

        when(attendancePolicyQueryService.findById(id))
                .thenThrow(new NotFoundException("Attendance policy not found with id: " + id));

        assertThrows(NotFoundException.class, () -> attendancePolicyDomainService.update(id, dto));
        verify(attendancePolicyValidator, never()).validateUpdate(any(), any());
        verify(attendancePolicyQueryService, never()).save(any());
    }
}
