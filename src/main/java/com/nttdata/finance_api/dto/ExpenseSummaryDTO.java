package com.nttdata.finance_api.dto;

import java.math.BigDecimal;

public class ExpenseSummaryDTO {

    private Object label;
    private BigDecimal total;

    public ExpenseSummaryDTO(Object label, BigDecimal total) {
        this.label = label;
        this.total = total;
    }

    public Object getLabel() {
        return label;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
