package com.mastercard.dps.springbootdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.Serializable;
import java.util.UUID;


public class SignCSRResponse implements Serializable  {
    String data;
    private ObjectMapper mapper = new ObjectMapper();
    private ObjectNode reply;

    public SignCSRResponse(String data) {
        this.data = data;
        // create a JSON object
        reply = mapper.createObjectNode();
        reply.put("id", this.getId());
        reply.put("data", data);
    }

    public String getId() {
        return UUID.randomUUID().toString();
    }

    public String getData() {
        return this.data;
    }

    @Override
    public String toString() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(reply);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            return "Parsing Error";
        }
    }

}
