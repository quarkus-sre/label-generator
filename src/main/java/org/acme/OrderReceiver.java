package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.acme.domain.Order;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class OrderReceiver {
  
    @Incoming("orders-in")
    public void consume(Order order) {
        System.out.println(order.getCustomer());
    }
    
}
