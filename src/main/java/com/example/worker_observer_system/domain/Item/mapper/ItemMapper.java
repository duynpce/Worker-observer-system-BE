package com.example.worker_observer_system.domain.Item.mapper;

import com.example.worker_observer_system.domain.Item.Item;
import com.example.worker_observer_system.domain.Item.dto.GetItemDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    GetItemDto toDto(Item item);

    List<GetItemDto> toDtoList(List<Item> items);
}

