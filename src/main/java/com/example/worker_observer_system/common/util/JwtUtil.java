package com.example.worker_observer_system.common.util;

import com.example.worker_observer_system.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtUtil {
    public String getUuid() {
        if(SecurityContextHolder.getContext() == null ||  SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new UnauthorizedException("not logged in");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }
}
