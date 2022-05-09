package com.redhat.quarkus.sre;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.quarkus.sre.domain.Order;
import com.redhat.quarkus.sre.sender.OrderPackageSender;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class OrderReceiver {

    @Inject
    private OrderPackageSender sender;

    @Inject
    MeterRegistry registry;
  
    @Incoming("orders-in")
    @Blocking
    public void consume(Order order) {
        Timer timer = registry.timer("com.redhat.quarkus.sre.tempo.consumo");
        System.out.println("OrderReceiver.consume()");
        sender.send(order);
        timer.record(Duration.between(order.getCreationDateTime(), LocalDateTime.now()));
    }
    
}
