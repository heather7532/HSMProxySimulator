package com.mastercard.dps.springbootdemo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SignCSRController {

    //private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private RabbitTemplate rabbitTemplate;
    private Receiver receiver;


    public SignCSRController(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping("/signcsr")
    public SignCSRResponse signCSR(@RequestParam(value = "msg",
            defaultValue = "Do you like my new cert?") String msg) throws InterruptedException {

        this.receiver = receiver;
        rabbitTemplate = rabbitTemplate;

        System.out.println("Sending message...");
        //rabbitTemplate.convertAndSend(ClientConfiguration.topicExchangeName, "tc.proxy.test", msg);
        SignCSRResponse response = null;
        response = new SignCSRResponse(msg);
        rabbitTemplate.convertAndSend(ClientConfiguration.topicExchangeName, "tc.proxy.test", response);
        //receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        return response;
    }
}