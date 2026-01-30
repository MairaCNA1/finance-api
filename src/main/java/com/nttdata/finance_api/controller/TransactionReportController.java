package com.nttdata.finance_api.controller;

import com.nttdata.finance_api.service.TransactionReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class TransactionReportController {

    private final TransactionReportService service;

    public TransactionReportController(TransactionReportService service) {
        this.service = service;
    }


    @PreAuthorize("@userSecurity.isOwner(#userId)")
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long userId) {

        byte[] file = service.generateExcelReport(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=transactions.xlsx")
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .body(file);
    }
}