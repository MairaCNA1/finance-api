package com.nttdata.finance_api.service;

import com.nttdata.finance_api.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ExchangeRateResponse getExchangeRate(String currency, String date) {

        String url = String.format(
                "https://brasilapi.com.br/api/cambio/v1/cotacao/%s/%s",
                currency,
                date
        );

        Map<String, Object> response =
                restTemplate.getForObject(url, Map.class);

        if (response == null || response.get("cotacoes") == null) {
            throw new RuntimeException("Exchange rate not available");
        }

        List<Map<String, Object>> cotacoes =
                (List<Map<String, Object>>) response.get("cotacoes");

        if (cotacoes.isEmpty()) {
            throw new RuntimeException("Exchange rate not available");
        }

        Map<String, Object> firstQuote = cotacoes.get(0);

        BigDecimal rate =
                new BigDecimal(firstQuote.get("cotacao_venda").toString());

        LocalDate parsedDate = LocalDate.parse(date);

        return new ExchangeRateResponse(currency, parsedDate, rate);
    }
}
