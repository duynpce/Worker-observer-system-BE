package com.example.worker_observer_system.domain.Item.dto;

import com.example.worker_observer_system.common.dto.PaginationDto;
import com.example.worker_observer_system.domain.account.constant.Station;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemFilter {

    private Station currentStation;

    @Valid
    @NotNull(message = "paginationDto is required")
    private PaginationDto paginationDto;
}

