package account;

import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.constant.Role;
import com.example.worker_observer_system.domain.account.constant.Station;
import com.example.worker_observer_system.domain.account.dto.CreateAccountDto;
import com.example.worker_observer_system.domain.account.dto.UpdateAccountDto;
import lombok.Getter;

import java.util.UUID;

public class AccountTestCases {

    private static AccountTestCases instance;

    @Getter
    private final Account accountTestCase = new Account();

    {
        accountTestCase.setId(UUID.fromString("00000000-0000-6000-8000-000000000001"));
        accountTestCase.setName("John Doe");
        accountTestCase.setEmail("john.doe@test.com");
        accountTestCase.setPassword("hashedPassword");
        accountTestCase.setStation(Station.STATION_A);
        accountTestCase.setRole(Role.WORKER);
    }

    public static AccountTestCases getInstance() {
        if (instance == null) {
            instance = new AccountTestCases();
        }
        return instance;
    }

    public CreateAccountDto getCreateAccountDto() {
        return new CreateAccountDto(
                "John Doe",
                "john.doe@test.com",
                "password123",
                Station.STATION_A,
                Role.WORKER
        );
    }

    public CreateAccountDto getDuplicateEmailCreateAccountDto() {
        return new CreateAccountDto(
                "Jane Doe",
                "john.doe@test.com",
                "password456",
                Station.STATION_B,
                Role.FLOOR_MANAGER
        );
    }

    public UpdateAccountDto getUpdateAccountDto() {
        return new UpdateAccountDto(
                "John Updated",
                "john.updated@test.com",
                "newpassword123",
                Station.STATION_B,
                Role.FLOOR_MANAGER
        );
    }

    public UpdateAccountDto getPartialUpdateAccountDto() {
        return new UpdateAccountDto(
                "John Partial",
                null,
                null,
                null,
                null
        );
    }
}
