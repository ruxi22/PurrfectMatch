package com.purrfect.adoption_service.messaging;

import com.purrfect.adoption_service.dto.AdoptionCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AdoptionEventProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name:adoption.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key:adoption.created}")
    private String routingKey;

    public void sendAdoptionCreatedEvent(AdoptionCreatedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}






