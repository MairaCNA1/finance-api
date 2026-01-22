package com.nttdata.finance_api.service;

import com.nttdata.finance_api.dto.BankBalanceResponse;
import com.nttdata.finance_api.exception.BusinessException;
import com.nttdata.finance_api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankBalanceService {

    @Value("${external.mockapi.base-url}")
    private String mockApiBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public BankBalanceResponse getBalanceByUser(Long userId) {

        String url = mockApiBaseUrl + "/balances";

        BankBalanceResponse[] response =
                restTemplate.getForObject(url, BankBalanceResponse[].class);

        if (response == null) {
            throw new BusinessException("Balance service unavailable");
        }

        for (BankBalanceResponse balance : response) {
            if (balance.getUserId().equals(userId)) {
                return balance;
            }
        }

        throw new ResourceNotFoundException(
                "Bank balance not found for user id: " + userId);
    }
}
