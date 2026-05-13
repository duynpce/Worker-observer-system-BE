package com.example.worker_observer_system.domain.Item.controller;

import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.domain.Item.dto.CreateItemBatchDto;
import com.example.worker_observer_system.domain.Item.dto.GetItemDto;
import com.example.worker_observer_system.domain.Item.dto.ItemFilter;
import com.example.worker_observer_system.domain.Item.service.domain.ItemDomainService;
import com.example.worker_observer_system.domain.account.constant.Station;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemDomainService itemDomainService;

    @PostMapping("/batch")
    public ResponseEntity<ResponseDto<Void>> createBatch(@Valid @RequestBody CreateItemBatchDto dto) {
        itemDomainService.createBatch(dto);
        return ResponseEntity.ok(ResponseDto.success(null, "Items created successfully"));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<Void>> updateStation(
            @RequestParam UUID id, @RequestParam Station station
            ) {
        itemDomainService.updateStation(id, station);
        return ResponseEntity.ok(ResponseDto.success(null, "Item updated to station: " + station));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetItemDto>>> getByFilter(
            @ModelAttribute ItemFilter filter
    ) {
        ResponseDto<List<GetItemDto>> response = itemDomainService.getByFilter(filter);
        return ResponseEntity.ok(response);
    }
}

