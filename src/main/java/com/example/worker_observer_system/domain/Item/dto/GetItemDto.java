package com.example.worker_observer_system.domain.Item.dto;

import com.example.worker_observer_system.domain.account.constant.Station;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class GetItemDto {
    private UUID id;
    private Station currentStation;
    private Instant createdAt;
}

