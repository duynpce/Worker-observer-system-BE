package attendance;
import com.example.worker_observer_system.common.dto.PaginationDto;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceStatus;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import com.example.worker_observer_system.domain.attendance.dto.CreateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.dto.DailyAttendanceFilter;
import com.example.worker_observer_system.domain.attendance.dto.UpdateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import com.example.worker_observer_system.domain.attendance.entity.DailyAttendance;
import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceTestCases {
    private static AttendanceTestCases instance;

    public static AttendanceTestCases getInstance() {
        if (instance == null) {
            instance = new AttendanceTestCases();
        }
        return instance;
    }

    public AttendancePolicy getAttendancePolicyTestCase() {
        AttendancePolicy policy = new AttendancePolicy();
        policy.setId(1L);
        policy.setEffectiveFrom(LocalDate.now().minusDays(30));
        policy.setEffectiveTo(LocalDate.now().plusDays(30));
        policy.setStartTime(LocalTime.of(8, 0));
        policy.setEndTime(LocalTime.of(17, 0));
        return policy;
    }

    public CreateAttendancePolicyDto getCreateAttendancePolicyDto() {
        CreateAttendancePolicyDto dto = new CreateAttendancePolicyDto();
        dto.setEffectiveFrom(LocalDate.now().minusDays(30));
        dto.setEffectiveTo(LocalDate.now().plusDays(30));
        dto.setStartTime(LocalTime.of(8, 0));
        dto.setEndTime(LocalTime.of(17, 0));
        return dto;
    }

    public CreateAttendancePolicyDto getOverlappingAttendancePolicyDto() {
        CreateAttendancePolicyDto dto = new CreateAttendancePolicyDto();
        dto.setEffectiveFrom(LocalDate.now().minusDays(10));
        dto.setEffectiveTo(LocalDate.now().plusDays(10));
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(18, 0));
        return dto;
    }

    public UpdateAttendancePolicyDto getUpdateAttendancePolicyDto() {
        UpdateAttendancePolicyDto dto = new UpdateAttendancePolicyDto();
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(18, 0));
        return dto;
    }

    public DailyAttendanceFilter getDailyAttendanceFilter() {
        DailyAttendanceFilter filter = new DailyAttendanceFilter();
        filter.setAttendanceDate(LocalDate.now());
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);
        filter.setPaginationDto(paginationDto);
        return filter;
    }

    public DailyAttendanceFilter getOutOfRangeDailyAttendanceFilter() {
        DailyAttendanceFilter filter = new DailyAttendanceFilter();
        filter.setAttendanceDate(LocalDate.now());
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(1000);
        paginationDto.setLimit(10);
        filter.setPaginationDto(paginationDto);
        return filter;
    }

    public AttendancePolicy getPolicyForOnTime() {
        AttendancePolicy policy = new AttendancePolicy();
        policy.setId(1L);
        policy.setEffectiveFrom(LocalDate.now().minusDays(30));
        policy.setEffectiveTo(LocalDate.now().plusDays(30));
        policy.setStartTime(LocalTime.now().plusHours(1));
        policy.setEndTime(LocalTime.now().minusHours(1));
        return policy;
    }

    public AttendancePolicy getPolicyForLate() {
        AttendancePolicy policy = new AttendancePolicy();
        policy.setId(1L);
        policy.setEffectiveFrom(LocalDate.now().minusDays(30));
        policy.setEffectiveTo(LocalDate.now().plusDays(30));
        policy.setStartTime(LocalTime.now().minusHours(1));
        policy.setEndTime(LocalTime.now().plusHours(1));
        return policy;
    }

    public DailyAttendance getDailyAttendanceTestCase() {
        DailyAttendance da = new DailyAttendance();
        da.setAttendanceDate(LocalDate.now());
        da.setAttendanceTime(LocalTime.now());
        da.setStatus(AttendanceStatus.LATE);
        da.setType(AttendanceType.START_TIME);
        return da;
    }
}