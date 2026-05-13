package item;

import com.example.worker_observer_system.common.dto.PaginationDto;
import com.example.worker_observer_system.domain.Item.Item;
import com.example.worker_observer_system.domain.Item.dto.CreateItemBatchDto;
import com.example.worker_observer_system.domain.Item.dto.ItemFilter;
import com.example.worker_observer_system.domain.account.constant.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemTestCases {

    private static ItemTestCases instance;

    public static ItemTestCases getInstance() {
        if (instance == null) {
            instance = new ItemTestCases();
        }
        return instance;
    }

    public Item getItemTestCase() {
        Item item = new Item();
        item.setId(UUID.fromString("00000000-0000-6000-8000-000000000002"));
        item.setCurrentStation(Station.STATION_A);
        return item;
    }

    public CreateItemBatchDto getCreateItemBatchDto() {
        CreateItemBatchDto dto = new CreateItemBatchDto();
        dto.setCount(3);
        return dto;
    }

    public CreateItemBatchDto getCreateSingleItemBatchDto() {
        CreateItemBatchDto dto = new CreateItemBatchDto();
        dto.setCount(1);
        return dto;
    }

    public ItemFilter getItemFilter() {
        ItemFilter filter = new ItemFilter();
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);
        filter.setPaginationDto(paginationDto);
        return filter;
    }

    public ItemFilter getItemFilterByStation() {
        ItemFilter filter = new ItemFilter();
        filter.setCurrentStation(Station.STATION_A);
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(0);
        paginationDto.setLimit(10);
        filter.setPaginationDto(paginationDto);
        return filter;
    }

    public ItemFilter getOutOfRangeItemFilter() {
        ItemFilter filter = new ItemFilter();
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPage(1000);
        paginationDto.setLimit(10);
        filter.setPaginationDto(paginationDto);
        return filter;
    }

    public List<Item> getListOfItems(int numberOfItems) {
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < numberOfItems; i++) {
            Item item = new Item();
            item.setId(UUID.fromString(String.format("00000000-0000-6000-8000-00000000000%d", i + 1)));
            item.setCurrentStation(Station.STATION_A);
            items.add(item);
        }
        return items;
    }
}

