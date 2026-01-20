package com.nttdata.finance_api.dto;

import java.util.List;

public class BrasilApiExchangeResponse {

    private String moeda;
    private String data;
    private List<BrasilApiQuoteDTO> cotacoes;

    public String getMoeda() {
        return moeda;
    }

    public String getData() {
        return data;
    }

    public List<BrasilApiQuoteDTO> getCotacoes() {
        return cotacoes;
    }
}
