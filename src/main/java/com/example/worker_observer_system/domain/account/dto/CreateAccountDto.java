package com.example.worker_observer_system.domain.account.dto;

import com.example.worker_observer_system.domain.account.constant.Role;
import com.example.worker_observer_system.domain.account.constant.Station;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountDto(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull Station station,
        @NotNull Role role
) {}
