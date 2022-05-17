package com.redhat.quarkus.sre.label;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.quarkus.sre.label.domain.Order;
import com.redhat.quarkus.sre.label.sender.OrderPackageSender;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentracing.Tracer;
import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
@Traced
public class OrderReceiver {

    @Inject
    private OrderPackageSender sender;

    @Inject
    Logger logger;
    
    @Inject
    Tracer configuredTracer;

    private Timer timer;

    OrderReceiver(MeterRegistry registry) {
        this.timer = registry.timer("sre.label-generator.tempo.consumo");
    }
  
    @Incoming("orders-in")
    @Blocking(value="myworkerpool", ordered = false)
    public CompletionStage<Void> consume(Message<Order> orderMessage) {
        Order order = orderMessage.getPayload();
        
        sender.send(order);

        Duration between = Duration.between(order.getCreationDateTime(), LocalDateTime.now());
        timer.record(between);
        logger.infof("Duration in millis: %s", between.toMillis());

        return orderMessage.ack();
    }
    
}
