package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.Role;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
import com.nttdata.finance_api.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void shouldReturnUserWhenUserExists() {

        User user = new User(
                "Maria Silva",
                "maria.silva@email.com",
                "senhaFake",
                Role.USER
        );

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = service.findById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Maria Silva", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(1L)
        );
    }
}
