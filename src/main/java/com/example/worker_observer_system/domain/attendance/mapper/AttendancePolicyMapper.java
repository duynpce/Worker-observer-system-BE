package com.example.worker_observer_system.domain.attendance.mapper;
import com.example.worker_observer_system.domain.attendance.dto.CreateAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.dto.GetAttendancePolicyDto;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface AttendancePolicyMapper {
    @Mapping(target = "id", ignore = true)
    AttendancePolicy toEntity(CreateAttendancePolicyDto dto);

    GetAttendancePolicyDto toDto(AttendancePolicy policy);
}