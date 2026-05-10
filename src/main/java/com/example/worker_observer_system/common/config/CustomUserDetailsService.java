package com.example.worker_observer_system.common.config;

import com.example.worker_observer_system.common.exception.NotFoundException;
import com.example.worker_observer_system.domain.account.Account;
import com.example.worker_observer_system.domain.account.Service.AccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountQueryService accountQueryService;


    @Override
    /// here, username is UUID because login by scanning QR code --> do not need a username
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Account account = accountQueryService.findById(UUID.fromString(username));
            return User.builder()
                    .username(account.getId().toString())
                    .password(account.getPassword())
                    .roles(account.getRole().name())
                    .build();
        }catch(NotFoundException e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
