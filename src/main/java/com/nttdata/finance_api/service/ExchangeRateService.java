package com.nttdata.finance_api.service;

import com.nttdata.finance_api.dto.BrasilApiExchangeResponse;
import com.nttdata.finance_api.dto.BrasilApiQuoteDTO;
import com.nttdata.finance_api.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ExchangeRateService {

    private static final String BRASIL_API_URL =
            "https://brasilapi.com.br/api/cambio/v1/cotacao/{currency}/{date}";

    private final RestTemplate restTemplate = new RestTemplate();

    public ExchangeRateResponse getExchangeRate(String currency) {

        LocalDate date = LocalDate.now().minusDays(1);

        BrasilApiExchangeResponse response = restTemplate.getForObject(
                BRASIL_API_URL,
                BrasilApiExchangeResponse.class,
                currency,
                date.toString()
        );

        if (response == null || response.getCotacoes() == null || response.getCotacoes().isEmpty()) {
            throw new RuntimeException("Exchange rate not available");
        }

        BrasilApiQuoteDTO quote = response.getCotacoes().get(0);
        BigDecimal rate = quote.getCotacao_venda();

        if (rate == null) {
            throw new RuntimeException("Exchange rate value not found");
        }

        return new ExchangeRateResponse(currency, date, rate);
    }
}
