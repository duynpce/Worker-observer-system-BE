package item.integration;

import com.example.worker_observer_system.common.IntegrationTest;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.domain.Item.controller.ItemController;
import com.example.worker_observer_system.domain.Item.dto.CreateItemBatchDto;
import com.example.worker_observer_system.domain.Item.dto.GetItemDto;
import com.example.worker_observer_system.domain.Item.dto.ItemFilter;
import com.example.worker_observer_system.domain.account.constant.Station;
import item.ItemTestCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemIntegrationTest extends IntegrationTest {

    @Autowired
    private ItemController itemController;

    private final ItemTestCases itemTestCases = ItemTestCases.getInstance();
    
    

    @Test
    void createBatchSuccess() {
        CreateItemBatchDto dto = itemTestCases.getCreateItemBatchDto();

        ResponseEntity<ResponseDto<Void>> response = itemController.createBatch(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Items created successfully", response.getBody().getMessage(), "Success message should match");
    }

    @Test
    void createBatchSuccess_singleItem() {
        CreateItemBatchDto dto = itemTestCases.getCreateSingleItemBatchDto();

        ResponseEntity<ResponseDto<Void>> response = itemController.createBatch(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Items created successfully", response.getBody().getMessage(), "Success message should match");
    }

    @Test
    void getByFilterSuccess() {
        itemController.createBatch(itemTestCases.getCreateItemBatchDto());

        ItemFilter filter = itemTestCases.getItemFilter();
        ResponseEntity<ResponseDto<List<GetItemDto>>> response = itemController.getByFilter(filter);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Items retrieved successfully", response.getBody().getMessage(), "Success message should match");
        assertNotNull(response.getBody().getData(), "Response data should not be null");
        assertFalse(response.getBody().getData().isEmpty(), "Response data should not be empty");
        assertNotNull(response.getBody().getMetaData(), "Metadata should not be null");
        assertTrue(response.getBody().getMetaData().getTotalItems() > 0, "Total items should be greater than 0");
        response.getBody().getData().forEach(item ->
                assertEquals(Station.STATION_A, item.getCurrentStation(), "All items should be at STATION_A"));
    }

    @Test
    void getByFilterSuccess_byStation() {
        itemController.createBatch(itemTestCases.getCreateItemBatchDto());

        ItemFilter filter = itemTestCases.getItemFilterByStation();
        ResponseEntity<ResponseDto<List<GetItemDto>>> response = itemController.getByFilter(filter);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertFalse(response.getBody().getData().isEmpty(), "Response data should not be empty");
        response.getBody().getData().forEach(item ->
                assertEquals(Station.STATION_A, item.getCurrentStation(), "All filtered items should be at STATION_A"));
    }

    @Test
    void getByFilterEmpty_outOfRange() {
        itemController.createBatch(itemTestCases.getCreateItemBatchDto());

        ItemFilter filter = itemTestCases.getOutOfRangeItemFilter();
        ResponseEntity<ResponseDto<List<GetItemDto>>> response = itemController.getByFilter(filter);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertTrue(response.getBody().getData().isEmpty(), "Response data should be empty for out-of-range page");
    }
}

