package attendance.integration;

import attendance.AttendanceTestCases;
import com.example.worker_observer_system.common.IntegrationTest;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.common.exception.ValidationException;
import com.example.worker_observer_system.domain.attendance.controller.AttendancePolicyController;
import com.example.worker_observer_system.domain.attendance.dto.CreateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.dto.UpdateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import com.example.worker_observer_system.domain.attendance.service.query.AttendancePolicyQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class AttendancePolicyIntegrationTest extends IntegrationTest {

    @Autowired
    private AttendancePolicyController attendancePolicyController;

    @Autowired
    private AttendancePolicyQueryService attendancePolicyQueryService;

    private AttendanceTestCases attendanceTestCases;

    @BeforeEach
    void setUp() {
        attendanceTestCases = AttendanceTestCases.getInstance();
    }

    @Test
    void createPolicySuccess() {
        CreateAttendancePolicyDto dto = attendanceTestCases.getCreateAttendancePolicyDto();

        ResponseEntity<ResponseDto<String>> response = attendancePolicyController.create(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Attendance policy created successfully", response.getBody().getMessage(), "Success message should match");
    }

    @Test
    void createPolicyFail_overlap() {
        CreateAttendancePolicyDto dto = attendanceTestCases.getCreateAttendancePolicyDto();
        attendancePolicyController.create(dto);

        CreateAttendancePolicyDto overlappingDto = attendanceTestCases.getOverlappingAttendancePolicyDto();
        assertThrows(ValidationException.class, () -> attendancePolicyController.create(overlappingDto),
                "Should throw ValidationException for overlapping policy");
    }

    @Test
    void updatePolicySuccess() {
        CreateAttendancePolicyDto createDto = attendanceTestCases.getCreateAttendancePolicyDto();
        attendancePolicyController.create(createDto);

        AttendancePolicy savedPolicy = attendancePolicyQueryService.findActivePolicyForDate(LocalDate.now());
        Long id = savedPolicy.getId();

        UpdateAttendancePolicyDto updateDto = attendanceTestCases.getUpdateAttendancePolicyDto();
        ResponseEntity<ResponseDto<String>> response = attendancePolicyController.update(id, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Attendance policy updated successfully", response.getBody().getMessage(), "Success message should match");

        AttendancePolicy updatedPolicy = attendancePolicyQueryService.findById(id);
        assertEquals(LocalTime.of(9, 0), updatedPolicy.getStartTime(), "Start time should be updated");
        assertEquals(LocalTime.of(18, 0), updatedPolicy.getEndTime(), "End time should be updated");
    }

    @Test
    void updatePolicyFail_notFound() {
        Long nonExistentId = 99999L;
        UpdateAttendancePolicyDto updateDto = attendanceTestCases.getUpdateAttendancePolicyDto();

        assertThrows(NotFoundException.class, () -> attendancePolicyController.update(nonExistentId, updateDto),
                "Should throw NotFoundException for non-existent policy id");
    }
}
