package com.example.worker_observer_system.domain.account.Service;

import com.example.worker_observer_system.common.util.JwtUtil;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.AccountMapper;
import com.example.worker_observer_system.domain.account.AccountValidator;
import com.example.worker_observer_system.domain.account.dto.CreateAccountDto;
import com.example.worker_observer_system.domain.account.dto.UpdateAccountDto;
import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountDomainService {

    private final AccountMapper accountMapper;
    private final AccountQueryService accountQueryService;
    private final AccountValidator accountValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public Account create(CreateAccountDto dto) {
        Account account = accountMapper.toEntity(dto);
        account.setId(Generators.timeBasedReorderedGenerator().generate());
        account.setPassword(passwordEncoder.encode(dto.password()));
        accountValidator.validateCreate(account);
        return accountQueryService.save(account);
    }

    @Transactional
    public Account update( UpdateAccountDto dto) {
        UUID id = UUID.fromString(jwtUtil.getUuid());
        Account existingAccount = accountQueryService.findById(id);
        accountValidator.validateUpdate(dto, existingAccount);
        if (dto.password() != null) {
            existingAccount.setPassword(passwordEncoder.encode(dto.password()));
        }
        return accountQueryService.save(existingAccount);
    }
}
