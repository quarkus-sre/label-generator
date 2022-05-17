package com.redhat.quarkus.sre.label;


import com.redhat.quarkus.sre.label.domain.Order;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OrderDeserializer extends ObjectMapperDeserializer<Order> {
    public OrderDeserializer() {
        super(Order.class);
    }
}