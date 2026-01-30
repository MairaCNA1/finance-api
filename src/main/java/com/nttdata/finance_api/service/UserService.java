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

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(CreateUserRequest request) {
        return createUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                Role.USER
        );
    }

    public User createUser(String name,
                           String email,
                           String rawPassword,
                           Role role) {

        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado: " + email);
        }

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

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo CSV não enviado");
        }

        int total = 0;
        int success = 0;
        int failed = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                total++;

                try {
                    String[] data = line.split(",");

                    if (data.length < 3) {
                        failed++;
                        continue;
                    }

                    String name = data[0].trim();
                    String email = data[1].trim();
                    String password = data[2].trim();


                    if (repository.findByEmail(email).isPresent()) {
                        failed++;
                        continue;
                    }

                    createUser(name, email, password, Role.USER);
                    success++;

                } catch (Exception e) {
                    failed++;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo CSV", e);
        }

        return new UserUploadResponse(total, success, failed);
    }
}