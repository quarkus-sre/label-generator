package com.redhat.quarkus.sre;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.redhat.quarkus.sre.domain.Order;
import com.redhat.quarkus.sre.sender.OrderPackageSender;

import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class OrderReceiver {

    @Inject
    private OrderPackageSender sender;
  
    @Incoming("orders-in")
    @Transactional
    public void consume(Order order) {
        System.out.println(order.getCustomer());
    }
    
}
