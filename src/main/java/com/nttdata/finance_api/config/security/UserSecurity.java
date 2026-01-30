package com.nttdata.finance_api.config.security;

import com.nttdata.finance_api.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    private final UserRepository userRepository;

    public UserSecurity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long userId) {


        if (SecurityUtils.hasRole("ADMIN")) {
            return true;
        }

        String email = SecurityUtils.getLoggedUserEmail();
        if (email == null) return false;

        return userRepository.findById(userId)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }
}