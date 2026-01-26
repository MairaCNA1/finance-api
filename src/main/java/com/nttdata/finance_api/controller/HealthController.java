package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {

        return ResponseEntity.ok(new ApiResponse<>(
                200,
                "Aplicação no ar",
                "OK"
        ));
    }
}
