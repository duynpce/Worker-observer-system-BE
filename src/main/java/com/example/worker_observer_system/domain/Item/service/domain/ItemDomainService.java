package com.example.worker_observer_system.domain.Item.service.domain;

import com.example.worker_observer_system.common.dto.MetaDataDto;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.exception.ValidationException;
import com.example.worker_observer_system.domain.Item.Item;
import com.example.worker_observer_system.domain.Item.dto.CreateItemBatchDto;
import com.example.worker_observer_system.domain.Item.dto.GetItemDto;
import com.example.worker_observer_system.domain.Item.dto.ItemFilter;
import com.example.worker_observer_system.domain.Item.mapper.ItemMapper;
import com.example.worker_observer_system.domain.Item.service.query.ItemQueryService;
import com.example.worker_observer_system.domain.account.constant.Station;
import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemDomainService {

    private final ItemQueryService itemQueryService;
    private final ItemMapper itemMapper;

    @Transactional
    public List<Item> createBatch(CreateItemBatchDto dto) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < dto.getCount(); i++) {
            Item item = new Item();
            item.setCurrentStation(Station.STATION_A);
            items.add(item);
        }
        return itemQueryService.saveAll(items);
    }

    @Transactional
    public Item updateStation(UUID itemId, Station newStation) {
        Item item = itemQueryService.findById(itemId);
        if(newStation.equals(item.getCurrentStation())) {
            throw new ValidationException("Item is already at station: " + newStation);
        }
        item.setCurrentStation(newStation);
        return itemQueryService.save(item);
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<GetItemDto>> getByFilter(ItemFilter filter) {
        Page<Item> itemPage = itemQueryService.findByFilter(filter);

        MetaDataDto metaData = MetaDataDto.builder()
                .totalItems(itemPage.getTotalElements())
                .totalPages(itemPage.getTotalPages())
                .paginationDto(filter.getPaginationDto())
                .build();

        return ResponseDto.success(itemMapper.toDtoList(itemPage.getContent()), "Items retrieved successfully", metaData);
    }
}

