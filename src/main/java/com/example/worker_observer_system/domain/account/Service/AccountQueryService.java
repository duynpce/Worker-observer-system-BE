package com.example.worker_observer_system.domain.account.Service;

import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account findById(UUID id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found with id: " + id)
        );
    }

    @Transactional(readOnly = true)
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("Account not found with email: " + email)
        );
    }

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
