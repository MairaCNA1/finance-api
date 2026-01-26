package com.nttdata.finance_api.config.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.finance_api.config.kafka.event.TransactionCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventProducer {

    private static final String TOPIC = "transaction-created";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransactionEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(TransactionCreatedEvent event) {

        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, payload);
        } catch (Exception e) {
            // NÃO quebra o fluxo principal
            System.err.println("Kafka error: " + e.getMessage());
        }
    }
}