package com.example.worker_observer_system.domain.account.dto;

import com.example.worker_observer_system.domain.account.constant.Role;
import com.example.worker_observer_system.domain.account.constant.Station;
import jakarta.validation.constraints.Email;

public record UpdateAccountDto(
        String name,
        @Email String email,
        String password,
        Station station,
        Role role
) {}
