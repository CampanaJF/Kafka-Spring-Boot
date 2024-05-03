package com.inkwell.emailms.service;

import com.inkwell.core.ProductCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "product-create-events-topic")
public class ProductCreateEventHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaHandler
    public void handle(ProductCreateEvent productCreateEvent){
        logger.info("Received a new Event " + productCreateEvent.getTitle());
    }
}
