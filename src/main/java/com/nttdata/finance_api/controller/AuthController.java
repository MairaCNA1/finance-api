package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.LoginRequest;
import com.nttdata.finance_api.dto.LoginResponse;
import com.nttdata.finance_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {

        String token = authService.login(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
