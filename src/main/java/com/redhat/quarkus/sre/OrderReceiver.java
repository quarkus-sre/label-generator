package com.redhat.quarkus.sre;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.quarkus.sre.domain.Order;
import com.redhat.quarkus.sre.sender.OrderPackageSender;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
@Traced
public class OrderReceiver {

    @Inject
    private OrderPackageSender sender;

    @Inject
    MeterRegistry registry;

    @Inject
    Logger logger;
    
    @Inject
    io.opentracing.Tracer configuredTracer;
  
    @Incoming("orders-in")
    @Blocking(value="myworkerpool", ordered = false)
    public CompletionStage<Void> consume(Message<Order> orderMessage) {
        Order order = orderMessage.getPayload();
        Timer timer = registry.timer("sre.label-generator.tempo.consumo");
        
        sender.send(order);

        Duration between = Duration.between(order.getCreationDateTime(), LocalDateTime.now());
        timer.record(between);
        logger.infof("Duration in millis: %s", between.toMillis());

        return orderMessage.ack();
    }
    
}
