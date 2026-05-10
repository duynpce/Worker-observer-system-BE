package com.example.worker_observer_system.domain.account;

import com.example.worker_observer_system.domain.account.dto.CreateAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    Account toEntity(CreateAccountDto dto);
}
