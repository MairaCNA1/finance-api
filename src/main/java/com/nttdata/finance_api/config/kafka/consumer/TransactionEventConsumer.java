package com.nttdata.finance_api.config.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConsumer {

    @KafkaListener(
            topics = "transaction.created",
            groupId = "finance-api-group"
    )
    public void listen(String message) {
        System.out.println("📩 Evento Kafka recebido: " + message);
    }
}