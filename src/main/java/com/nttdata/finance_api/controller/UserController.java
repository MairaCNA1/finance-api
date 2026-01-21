package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.ApiResponse;
import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // 1️⃣ Criar usuário
    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(
            @RequestBody @Valid User user) {

        User createdUser = service.create(user);

        ApiResponse<User> response =
                new ApiResponse<>(
                        201,
                        "User created successfully",
                        createdUser
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2️⃣ Listar usuários
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> list() {

        List<User> users = service.findAll();

        ApiResponse<List<User>> response =
                new ApiResponse<>(
                        200,
                        "Users retrieved successfully",
                        users
                );

        return ResponseEntity.ok(response);
    }

    // 3️⃣ Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findById(
            @PathVariable Long id) {

        User user = service.findById(id);

        ApiResponse<User> response =
                new ApiResponse<>(
                        200,
                        "User retrieved successfully",
                        user
                );

        return ResponseEntity.ok(response);
    }

    // 4️⃣ Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id) {

        service.delete(id);

        ApiResponse<Void> response =
                new ApiResponse<>(
                        204,
                        "User deleted successfully",
                        null
                );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
