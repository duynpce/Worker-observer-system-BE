package com.example.worker_observer_system.domain.Item.service.query;

import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.domain.Item.Item;
import com.example.worker_observer_system.domain.Item.dto.ItemFilter;
import com.example.worker_observer_system.domain.Item.repo.ItemRepository;
import com.example.worker_observer_system.domain.Item.specification.ItemSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemQueryService {

    private final ItemRepository itemRepository;

    @Transactional
    public List<Item> saveAll(List<Item> items) {
        return itemRepository.saveAll(items);
    }

    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Page<Item> findByFilter(ItemFilter filter) {
        Specification<Item> spec = ItemSpecification.withFilter(filter);
        PageRequest pageable = PageRequest.of(
                filter.getPaginationDto().getPage(),
                filter.getPaginationDto().getLimit()
        );
        return itemRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Item findById(UUID id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Item not found with id: " + id)
        );
    }

}

