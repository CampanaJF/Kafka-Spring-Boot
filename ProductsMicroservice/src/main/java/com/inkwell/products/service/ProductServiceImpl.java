package com.inkwell.products.service;

import com.inkwell.core.ProductCreateEvent;
import com.inkwell.products.model.Product;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final KafkaTemplate<String, ProductCreateEvent> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String createProductAsync(Product product) {

        String productId = UUID.randomUUID().toString();

        ProductCreateEvent productCreateEvent =
                new ProductCreateEvent(productId,product.getTitle(),product.getPrice(),product.getQuantity());

        CompletableFuture<SendResult<String, ProductCreateEvent>> future =
                kafkaTemplate.send("product-create-events-topic",productId, productCreateEvent);

        future.whenComplete((result, exception) -> {

            if(exception != null){
                logger.error("**********Failed to send message" + exception.getMessage());
            }else {
                logger.info("***********Metadata " + result.getRecordMetadata());
            }
        });

        logger.info("**************RETURNING PRODUCT ID****************");

        return productId;
    }

    @Override
    public String createProductSync(Product product) throws Exception{
        String productId = UUID.randomUUID().toString();

        ProductCreateEvent productCreateEvent =
                new ProductCreateEvent(productId,product.getTitle(),product.getPrice(),product.getQuantity());

        logger.info("**************BEFORE PUBLISHING****************");

        SendResult<String, ProductCreateEvent> result =
                kafkaTemplate.send("product-create-events-topic",productId, productCreateEvent).get();

        logger.info("***********PARTITION " + result.getRecordMetadata().partition());

        logger.info("***********TOPIC " + result.getRecordMetadata().topic());

        logger.info("***********OFFSET " + result.getRecordMetadata().offset());

        logger.info("**************RETURNING PRODUCT ID****************");

        return productId;
    }
}
