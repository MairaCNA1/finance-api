package com.nttdata.finance_api.config.kafka.producer;

import com.nttdata.finance_api.config.kafka.event.TransactionCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class TransactionCreatedListener {

    private static final Logger log =
            LoggerFactory.getLogger(TransactionCreatedListener.class);

    private final TransactionEventProducer producer;

    public TransactionCreatedListener(TransactionEventProducer producer) {
        this.producer = producer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(TransactionCreatedEvent event) {
        try {
            producer.send(event);
        } catch (Exception e) {
            log.warn("Kafka unavailable, event not sent", e);
        }
    }
}