package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {

        ApiResponse<String> response = new ApiResponse<>(
                200,
                "Application is running",
                "OK"
        );

        return ResponseEntity.ok(response);
    }
}
