package com.example.worker_observer_system.domain.account;

import com.example.worker_observer_system.common.exception.ConflictDataException;
import com.example.worker_observer_system.domain.account.dto.UpdateAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {

    private final AccountRepository accountRepository;

    public void validateCreate(Account account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new ConflictDataException("Account with email already exists: " + account.getEmail());
        }
    }

    public void validateUpdate(UpdateAccountDto dto, Account existingAccount) {
        if (dto.email() != null && !dto.email().equals(existingAccount.getEmail())) {
            if (accountRepository.existsByEmailAndIdNot(dto.email(), existingAccount.getId())) {
                throw new ConflictDataException("Account with email already exists: " + dto.email());
            }
        }

        if (dto.name() != null) existingAccount.setName(dto.name());
        if (dto.email() != null) existingAccount.setEmail(dto.email());
        if (dto.station() != null) existingAccount.setStation(dto.station());
        if (dto.role() != null) existingAccount.setRole(dto.role());
    }
}
