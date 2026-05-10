package account;

import com.example.worker_observer_system.common.UnitTest;
import com.example.worker_observer_system.common.exception.ConflictDataException;
import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.AccountMapper;
import com.example.worker_observer_system.domain.account.AccountValidator;
import com.example.worker_observer_system.domain.account.Service.AccountDomainService;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import com.example.worker_observer_system.domain.account.dto.CreateAccountDto;
import com.example.worker_observer_system.domain.account.dto.UpdateAccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountUnitTest extends UnitTest {

    @Mock
    private AccountMapper accountMapper;
    @Mock
    private AccountQueryService accountQueryService;
    @Mock
    private AccountValidator accountValidator;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountDomainService accountDomainService;

    private AccountTestCases accountTestCases;

    @BeforeEach
    void setUp() {
        accountTestCases = AccountTestCases.getInstance();
    }

    @Test
    void createAccountSuccess() {
        CreateAccountDto dto = accountTestCases.getCreateAccountDto();
        Account account = accountTestCases.getAccountTestCase();

        when(accountMapper.toEntity(dto)).thenReturn(account);
        when(passwordEncoder.encode(dto.password())).thenReturn("hashedPassword");
        when(accountQueryService.save(account)).thenReturn(account);

        Account result = accountDomainService.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("hashedPassword", result.getPassword());
        verify(accountValidator).validateCreate(account);
        verify(accountQueryService).save(account);
    }

    @Test
    void createAccountFail_duplicateEmail() {
        CreateAccountDto dto = accountTestCases.getCreateAccountDto();
        Account account = accountTestCases.getAccountTestCase();

        when(accountMapper.toEntity(dto)).thenReturn(account);
        when(passwordEncoder.encode(dto.password())).thenReturn("hashedPassword");
        doThrow(new ConflictDataException("Account with email already exists: " + dto.email()))
                .when(accountValidator).validateCreate(account);

        assertThrows(ConflictDataException.class, () -> accountDomainService.create(dto));
        verify(accountQueryService, never()).save(any());
    }

    @Test
    void updateAccountSuccess() {
        UUID id = accountTestCases.getAccountTestCase().getId();
        UpdateAccountDto dto = accountTestCases.getUpdateAccountDto();
        Account existingAccount = accountTestCases.getAccountTestCase();

        when(accountQueryService.findById(id)).thenReturn(existingAccount);
        when(passwordEncoder.encode(dto.password())).thenReturn("newHashedPassword");
        when(accountQueryService.save(existingAccount)).thenReturn(existingAccount);

        Account result = accountDomainService.update(id, dto);

        assertNotNull(result);
        verify(accountValidator).validateUpdate(dto, existingAccount);
        verify(accountQueryService).save(existingAccount);
    }

    @Test
    void updateAccountFail_notFound() {
        UUID randomId = UUID.randomUUID();
        UpdateAccountDto dto = accountTestCases.getUpdateAccountDto();

        when(accountQueryService.findById(randomId))
                .thenThrow(new NotFoundException("Account not found with id: " + randomId));

        assertThrows(NotFoundException.class, () -> accountDomainService.update(randomId, dto));
        verify(accountValidator, never()).validateUpdate(any(), any());
        verify(accountQueryService, never()).save(any());
    }
}
