package com.example.worker_observer_system.domain.attendance.mapper;
import com.example.worker_observer_system.domain.attendance.dto.GetDailyAttendanceDto;
import com.example.worker_observer_system.domain.attendance.entity.DailyAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
@Mapper(componentModel = "spring")
public interface DailyAttendanceMapper {


    DailyAttendance toEntity(GetDailyAttendanceDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountName", source = "account.name")
    @Mapping(target = "accountEmail", source = "account.email")
    GetDailyAttendanceDto toDto(DailyAttendance dailyAttendance);

    List<GetDailyAttendanceDto> toDtoList(List<DailyAttendance> dailyAttendances);
}
