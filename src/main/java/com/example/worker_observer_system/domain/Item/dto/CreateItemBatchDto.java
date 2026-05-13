package com.example.worker_observer_system.domain.Item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateItemBatchDto {

    @NotNull(message = "count is required")
    @Min(value = 1, message = "count must be at least 1")
    private Integer count;
}

