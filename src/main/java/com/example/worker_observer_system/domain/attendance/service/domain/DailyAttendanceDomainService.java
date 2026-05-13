package com.example.worker_observer_system.domain.attendance.service.domain;
import com.example.worker_observer_system.common.dto.MetaDataDto;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.exception.ConflictDataException;
import com.example.worker_observer_system.common.exception.ValidationException;
import com.example.worker_observer_system.common.util.JwtUtil;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceStatus;
import com.example.worker_observer_system.domain.attendance.constant.AttendanceType;
import com.example.worker_observer_system.domain.attendance.dto.DailyAttendanceFilter;
import com.example.worker_observer_system.domain.attendance.dto.GetDailyAttendanceDto;
import com.example.worker_observer_system.domain.attendance.entity.AttendancePolicy;
import com.example.worker_observer_system.domain.attendance.entity.DailyAttendance;
import com.example.worker_observer_system.domain.attendance.mapper.DailyAttendanceMapper;
import com.example.worker_observer_system.domain.attendance.service.query.AttendancePolicyQueryService;
import com.example.worker_observer_system.domain.attendance.service.query.DailyAttendanceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DailyAttendanceDomainService {
    private final DailyAttendanceQueryService dailyAttendanceQueryService;
    private final DailyAttendanceMapper dailyAttendanceMapper;
    private final AttendancePolicyQueryService attendancePolicyQueryService;
    private final AccountQueryService accountQueryService;
    private final JwtUtil jwtUtil;

    @Transactional
    public DailyAttendance checkAttendance(AttendanceType attendanceType) {
        UUID accountId = UUID.fromString(jwtUtil.getUuid());
        Account account = accountQueryService.findById(accountId);
        LocalDateTime now = LocalDateTime.now();
        AttendancePolicy policy = attendancePolicyQueryService.findActivePolicyForDate(now.toLocalDate());

        switch (attendanceType) {
            case START_TIME -> {
                return resolveStartTimeAttendance(now, policy, account);
            }
            case END_TIME -> {
                return resolveEndTimeAttendance(now, policy, account);
            }
            case null, default -> throw new ValidationException("invalid attendance type");

        }

    }

    private DailyAttendance resolveStartTimeAttendance(LocalDateTime now, AttendancePolicy policy, Account account) {
        if (dailyAttendanceQueryService.existsByAccountIdAndDateAndType(account.getId(), now.toLocalDate(), AttendanceType.START_TIME)) {
            throw new ConflictDataException("Account has already checked in today");
        }

        DailyAttendance dailyAttendance = new DailyAttendance();
        dailyAttendance.setType(AttendanceType.START_TIME);
        dailyAttendance.setAttendanceDate(now.toLocalDate());
        dailyAttendance.setAccount(account);
        dailyAttendance.setAttendanceTime(now.toLocalTime());

        if(policy.getStartTime().isBefore(now.toLocalTime())){
            dailyAttendance.setStatus(AttendanceStatus.LATE);
        }else{
            dailyAttendance.setStatus(AttendanceStatus.ON_TIME);
        }


        return dailyAttendanceQueryService.save(dailyAttendance);

    }

    private DailyAttendance resolveEndTimeAttendance(LocalDateTime now, AttendancePolicy policy, Account account) {
        Optional<DailyAttendance> existingDailyAttendance = dailyAttendanceQueryService.findByAccountIdAndDateAndType(
                account.getId(), now.toLocalDate(), AttendanceType.END_TIME
        );

        DailyAttendance dailyAttendance = existingDailyAttendance.orElseGet(DailyAttendance::new);

        dailyAttendance.setType(AttendanceType.END_TIME);
        dailyAttendance.setAttendanceDate(now.toLocalDate());
        dailyAttendance.setAttendanceTime(now.toLocalTime());
        dailyAttendance.setAccount(account);

        if (policy.getEndTime().isBefore(now.toLocalTime())) {
            dailyAttendance.setStatus(AttendanceStatus.ON_TIME_LEAVE);
        } else {
            dailyAttendance.setStatus(AttendanceStatus.EARLY_LEAVE);
        }

        return dailyAttendanceQueryService.save(dailyAttendance);
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<GetDailyAttendanceDto>> getByFilter(DailyAttendanceFilter filter) {
        List<DailyAttendance> attendances = dailyAttendanceQueryService.findByFilter(filter);
        List<Account> allAccounts = accountQueryService.findAll();
        Set<UUID> presentAccountIds = attendances.stream()
                .map(a -> a.getAccount().getId())
                .collect(Collectors.toSet());

        List<GetDailyAttendanceDto> result = new ArrayList<>(dailyAttendanceMapper.toDtoList(attendances));
        allAccounts.stream()
                .filter(acc -> !presentAccountIds.contains(acc.getId()))
                .map(acc -> buildAbsentDto(acc, filter))
                .forEach(result::add);

        int page = filter.getPaginationDto().getPage();
        int limit = filter.getPaginationDto().getLimit();
        int totalItems = result.size();
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int start = page * limit;
        int end = Math.min(start + limit, totalItems);
        List<GetDailyAttendanceDto> pagedResult = start >= totalItems ? Collections.emptyList() : result.subList(start, end);

        MetaDataDto metaData = MetaDataDto.builder()
                .totalItems(totalItems)
                .totalPages(totalPages)
                .paginationDto(filter.getPaginationDto())
                .build();

        return ResponseDto.success(pagedResult, "Daily attendances retrieved successfully", metaData);
    }



    private GetDailyAttendanceDto buildAbsentDto(Account account, DailyAttendanceFilter filter) {
        GetDailyAttendanceDto dto = new GetDailyAttendanceDto();
        dto.setAccountId(account.getId());
        dto.setAccountName(account.getName());
        dto.setAccountEmail(account.getEmail());
        dto.setAttendanceDate(filter.getAttendanceDate());
        dto.setStatus(AttendanceStatus.ABSENT);
        dto.setType(filter.getType());
        return dto;
    }
}