package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.domain.User;
import com.nttdata.finance_api.dto.ApiResponse;
import com.nttdata.finance_api.dto.CreateUserRequest;
import com.nttdata.finance_api.dto.UserUploadResponse;
import com.nttdata.finance_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(
            @RequestBody @Valid CreateUserRequest request) {

        User created = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        201,
                        "Usuário criado com sucesso",
                        created
                ));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> list() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Usuários listados",
                        service.findAll()
                )
        );
    }


    @PreAuthorize("@userSecurity.isOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Usuário encontrado",
                        service.findById(id)
                )
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<UserUploadResponse>> uploadUsers(
            @RequestParam("file") MultipartFile file) {

        UserUploadResponse result = service.uploadUsersFromCsv(file);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Upload concluído",
                        result
                )
        );
    }
}