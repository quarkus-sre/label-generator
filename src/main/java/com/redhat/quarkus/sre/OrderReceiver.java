package com.redhat.quarkus.sre;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.quarkus.sre.domain.Order;
import com.redhat.quarkus.sre.sender.OrderPackageSender;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class OrderReceiver {

    @Inject
    private OrderPackageSender sender;
  
    @Incoming("orders-in")
    @Blocking
    public void consume(Order order) {
        System.out.println("OrderReceiver.consume()");
        sender.send(order);
    }
    
}
