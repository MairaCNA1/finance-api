package com.nttdata.finance_api.service;

import com.nttdata.finance_api.dto.ExchangeRateResponse;
import com.nttdata.finance_api.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate;


    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ExchangeRateResponse getExchangeRate(String currency, String date) {

        String url = String.format(
                "https://brasilapi.com.br/api/cambio/v1/cotacao/%s/%s",
                currency,
                date
        );

        Map<String, Object> response =
                restTemplate.getForObject(url, Map.class);

        if (response == null || response.get("cotacoes") == null) {
            throw new BusinessException("Exchange rate not available");
        }

        List<Map<String, Object>> cotacoes =
                (List<Map<String, Object>>) response.get("cotacoes");

        if (cotacoes.isEmpty()) {
            throw new BusinessException(
                    "Exchange rate not available for the given date"
            );
        }

        Map<String, Object> firstQuote = cotacoes.get(0);

        Object sellRate = firstQuote.get("cotacao_venda");
        if (sellRate == null) {
            throw new BusinessException(
                    "Invalid exchange rate data returned by external service"
            );
        }

        BigDecimal rate = new BigDecimal(sellRate.toString());
        LocalDate parsedDate = LocalDate.parse(date);

        return new ExchangeRateResponse(currency, parsedDate, rate);
    }
}
