package account;

import com.example.worker_observer_system.common.IntegrationTest;
import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.common.exception.ConflictException;
import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.AccountController;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import com.example.worker_observer_system.domain.account.dto.CreateAccountDto;
import com.example.worker_observer_system.domain.account.dto.UpdateAccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountIntegrationTest extends IntegrationTest {

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountQueryService accountQueryService;

    private AccountTestCases accountTestCases;

    @BeforeEach
    void setUp() {
        accountTestCases = AccountTestCases.getInstance();
    }

    @Test
    void createAccountSuccess() {
        CreateAccountDto dto = accountTestCases.getCreateAccountDto();

        ResponseEntity<ResponseDto<String>> response = accountController.create(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Account created successfully", response.getBody().getMessage(), "Success message should match");

        Account saved = accountQueryService.findByEmail(dto.email());
        assertNotNull(saved.getId(), "Saved account should have an id");
        assertEquals(dto.name(), saved.getName(), "Saved account name should match");
        assertNotEquals(dto.password(), saved.getPassword(), "Password should be hashed");
    }

    @Test
    void createAccountFail_duplicateEmail() {
        CreateAccountDto dto = accountTestCases.getCreateAccountDto();
        accountController.create(dto);

        CreateAccountDto duplicateDto = accountTestCases.getDuplicateEmailCreateAccountDto();
        assertThrows(ConflictException.class, () -> accountController.create(duplicateDto),
                "Should throw ConflictException for duplicate email");
    }

    @Test
    void updateAccountSuccess() {
        CreateAccountDto createDto = accountTestCases.getCreateAccountDto();
        accountController.create(createDto);

        Account createdAccount = accountQueryService.findByEmail(createDto.email());
        String initialPassword =  createdAccount.getPassword();
        UUID id = createdAccount.getId();

        UpdateAccountDto updateDto = accountTestCases.getUpdateAccountDto();
        ResponseEntity<ResponseDto<String>> response = accountController.update(id, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isSuccess(), "Response success flag should be true");
        assertEquals("Account updated successfully", response.getBody().getMessage(), "Success message should match");

        Account updatedAccount = accountQueryService.findById(id);
        assertEquals(updateDto.name(), updatedAccount.getName(), "Name should be updated");
        assertEquals(updateDto.email(), updatedAccount.getEmail(), "Email should be updated");
        assertEquals(updateDto.station(), updatedAccount.getStation(), "Station should be updated");
        assertEquals(updateDto.role(), updatedAccount.getRole(), "Role should be updated");
        // because they in the same transaction
        assertNotEquals(initialPassword, updatedAccount.getPassword(), "Password should be re-hashed");
    }

    @Test
    void updateAccountFail_notFound() {
        UUID randomId = UUID.randomUUID();
        UpdateAccountDto updateDto = accountTestCases.getUpdateAccountDto();

        assertThrows(NotFoundException.class, () -> accountController.update(randomId, updateDto),
                "Should throw NotFoundException for non-existent account id");
    }
}
