package com.example.worker_observer_system.domain.Item.specification;

import com.example.worker_observer_system.domain.Item.Item;
import com.example.worker_observer_system.domain.Item.dto.ItemFilter;
import com.example.worker_observer_system.domain.account.constant.Station;
import org.springframework.data.jpa.domain.Specification;

public final class ItemSpecification {

    private ItemSpecification() {}

    public static Specification<Item> withFilter(ItemFilter filter) {
        return hasCurrentStation(filter.getCurrentStation());
    }

    private static Specification<Item> hasCurrentStation(Station station) {
        if (station == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("currentStation"), station);
    }
}

