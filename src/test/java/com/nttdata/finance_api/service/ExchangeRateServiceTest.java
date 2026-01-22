package com.nttdata.finance_api.service;

import com.nttdata.finance_api.dto.ExchangeRateResponse;
import com.nttdata.finance_api.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateService service;

    @Test
    void shouldReturnExchangeRateWhenApiReturnsData() {

        Map<String, Object> quote = Map.of(
                "cotacao_venda", "5.20"
        );

        Map<String, Object> apiResponse = Map.of(
                "cotacoes", List.of(quote)
        );

        when(restTemplate.getForObject(
                anyString(),
                eq(Map.class)
        )).thenReturn(apiResponse);

        ExchangeRateResponse response =
                service.getExchangeRate("USD", "2025-02-13");

        assertEquals("USD", response.getCurrency());
        assertEquals(LocalDate.of(2025, 2, 13), response.getDate());
        assertEquals(new BigDecimal("5.20"), response.getRate());
    }

    @Test
    void shouldThrowExceptionWhenApiReturnsNoData() {

        when(restTemplate.getForObject(
                anyString(),
                eq(Map.class)
        )).thenReturn(null);

        assertThrows(
                BusinessException.class,
                () -> service.getExchangeRate("USD", "2025-02-13")
        );
    }
}
