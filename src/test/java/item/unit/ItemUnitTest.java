package item.unit;

import com.example.worker_observer_system.common.UnitTest;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.domain.Item.Item;
import com.example.worker_observer_system.domain.Item.dto.CreateItemBatchDto;
import com.example.worker_observer_system.domain.Item.dto.GetItemDto;
import com.example.worker_observer_system.domain.Item.dto.ItemFilter;
import com.example.worker_observer_system.domain.Item.mapper.ItemMapper;
import com.example.worker_observer_system.domain.Item.service.domain.ItemDomainService;
import com.example.worker_observer_system.domain.Item.service.query.ItemQueryService;
import com.example.worker_observer_system.domain.account.constant.Station;
import item.ItemTestCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class ItemUnitTest extends UnitTest {

    @Mock
    private ItemQueryService itemQueryService;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemDomainService itemDomainService;

    private final ItemTestCases itemTestCases =ItemTestCases.getInstance();

    @Test
    void createBatchSuccess() {
        CreateItemBatchDto dto = itemTestCases.getCreateItemBatchDto();
        List<Item> itemsToSave = itemTestCases.getListOfItems(dto.getCount());
        when(itemQueryService.saveAll(anyList())).thenReturn(itemsToSave);


        List<Item> result = itemDomainService.createBatch(dto);

        assertNotNull(result);
        assertEquals(3, result.size(), "Should create 3 items");
        result.forEach(item -> {
            assertNotNull(item.getId(), "Each item should have a UUID");
            assertEquals(Station.STATION_A, item.getCurrentStation(), "Each item should be at STATION_A");
        });
        verify(itemQueryService).saveAll(anyList());
    }

    @Test
    void createBatchFail_saveAllThrowsException() {
        CreateItemBatchDto dto = itemTestCases.getCreateItemBatchDto();
        when(itemQueryService.saveAll(anyList())).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> itemDomainService.createBatch(dto));
        verify(itemQueryService).saveAll(anyList());
    }

    @Test
    void updateStationSuccess() {
        Item item = itemTestCases.getItemTestCase();
        UUID itemId = item.getId();
        Station newStation = Station.STATION_B;

        when(itemQueryService.findById(itemId)).thenReturn(item);
        when(itemQueryService.save(item)).thenReturn(item);

        Item result = itemDomainService.updateStation(itemId, newStation);

        assertNotNull(result);
        assertEquals(newStation, result.getCurrentStation(), "Station should be updated to STATION_B");
        verify(itemQueryService).findById(itemId);
        verify(itemQueryService).save(item);
    }

    @Test
    void updateStationFail_itemNotFound() {
        UUID unknownId = UUID.randomUUID();
        when(itemQueryService.findById(unknownId))
                .thenThrow(new NotFoundException("Item not found with id: " + unknownId));

        assertThrows(NotFoundException.class, () -> itemDomainService.updateStation(unknownId, Station.STATION_B));
        verify(itemQueryService, never()).save(any());
    }

    @Test
    void getByFilterSuccess() {
        Item item = itemTestCases.getItemTestCase();
        ItemFilter filter = itemTestCases.getItemFilter();
        GetItemDto itemDto = new GetItemDto();
        itemDto.setId(item.getId());
        itemDto.setCurrentStation(item.getCurrentStation());
        Page<Item> page = new PageImpl<>(List.of(item), PageRequest.of(0, 10), 1);

        when(itemQueryService.findByFilter(filter)).thenReturn(page);
        when(itemMapper.toDtoList(List.of(item))).thenReturn(List.of(itemDto));

        ResponseDto<List<GetItemDto>> response = itemDomainService.getByFilter(filter);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Items retrieved successfully", response.getMessage());
        assertEquals(1, response.getData().size());
        assertNotNull(response.getMetaData());
        assertEquals(1, response.getMetaData().getTotalItems());
    }

    @Test
    void getByFilterEmpty_outOfRange() {
        ItemFilter filter = itemTestCases.getOutOfRangeItemFilter();
        Page<Item> emptyPage = new PageImpl<>(List.of(), PageRequest.of(1000, 10), 0);

        when(itemQueryService.findByFilter(filter)).thenReturn(emptyPage);
        when(itemMapper.toDtoList(List.of())).thenReturn(List.of());

        ResponseDto<List<GetItemDto>> response = itemDomainService.getByFilter(filter);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty(), "Data should be empty for out-of-range page");
        assertEquals(0, response.getMetaData().getTotalItems());
    }
}

