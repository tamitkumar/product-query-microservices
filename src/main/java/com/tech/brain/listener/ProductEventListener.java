package com.tech.brain.listener;

import com.tech.brain.model.Product;
import com.tech.brain.model.ProductEvent;
import com.tech.brain.service.QueryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    private final QueryService queryService;

    public ProductEventListener(QueryService queryService) {
        this.queryService = queryService;
    }

    @KafkaListener(topics = "product-event-topic", groupId = "product-group")
    public void processProductEvent(ProductEvent message){
        System.out.println("📥 QueryService received message from Kafka: " + message);
        Product product = message.getProduct();
        if ("CREATE_EVENT".equalsIgnoreCase(message.getEventType())) {
            Product createdProduct = queryService.createProduct(product);
            System.out.println("Created product : " + createdProduct.getProductCode());
        }
        if ("UPDATE_EVENT".equalsIgnoreCase(message.getEventType())) {
            Product updatedProduct = queryService.updateProduct(product);
            System.out.println("Updated product : " + updatedProduct.getProductCode());
        }
        if ("DELETE_EVENT".equalsIgnoreCase(message.getEventType())) {
            String msg = queryService.deleteProduct(product);
            System.out.println(msg);
        }

    }
}
