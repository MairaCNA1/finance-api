package com.nttdata.finance_api.service;

import com.nttdata.finance_api.domain.Role;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.dto.CreateUserRequest;
import com.nttdata.finance_api.dto.UserUploadResponse;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
import com.nttdata.finance_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ usado no signup normal
    public User create(CreateUserRequest request) {
        return createUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                Role.USER
        );
    }

    // 🔧 método central reutilizável (CSV vai usar isso)
    public User createUser(
            String name,
            String email,
            String rawPassword,
            Role role
    ) {
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(
                name,
                email,
                encodedPassword,
                role
        );

        return repository.save(user);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + id
            );
        }
        repository.deleteById(id);
    }


    public UserUploadResponse uploadUsersFromCsv(MultipartFile file) {

        int total = 0;
        int success = 0;
        int failed = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                // pula cabeçalho
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                total++;

                try {
                    String[] data = line.split(",");

                    String name = data[0].trim();
                    String email = data[1].trim();
                    String password = data[2].trim();

                    createUser(name, email, password, Role.USER);
                    success++;

                } catch (Exception e) {
                    failed++;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo CSV");
        }

        return new UserUploadResponse(total, success, failed);
    }
}