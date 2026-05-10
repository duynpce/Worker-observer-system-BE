package com.example.worker_observer_system.domain.account;

import com.example.worker_observer_system.common.dto.ResponseDto;
import com.example.worker_observer_system.domain.account.Service.AccountDomainService;
import com.example.worker_observer_system.domain.account.dto.CreateAccountDto;
import com.example.worker_observer_system.domain.account.dto.UpdateAccountDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountDomainService accountDomainService;

    @PostMapping
    public ResponseEntity<ResponseDto<String>> create(@Valid @RequestBody CreateAccountDto dto) {
        accountDomainService.create(dto);
        return ResponseEntity.ok(ResponseDto.success(null, "Account created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAccountDto dto
    ) {
        accountDomainService.update(id, dto);
        return ResponseEntity.ok(ResponseDto.success(null, "Account updated successfully"));
    }
}
