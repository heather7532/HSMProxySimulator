package com.mastercard.emvcs.springbootdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mastercard.emvcs.SignCSRResponse;

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


    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET,
            value = "/signcsr")
    public SignCSRResponse signCSR(@RequestParam(value = "msg",
            defaultValue = "Do you like my new cert?") String msg) throws InterruptedException {

        this.receiver = receiver;
        rabbitTemplate = rabbitTemplate;

        System.out.println("Sending message...");
        SignCSRResponse response = null;
        response = new SignCSRResponse(msg);
        //make it a json string
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(response);
            System.out.println("ResultingJSONstring = " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        rabbitTemplate.convertAndSend(ClientConfiguration.topicExchangeName, ClientConfiguration.routingKey, json);
        System.out.println(response.toString());
        return response;
    }
}