package com.redhat.quarkus.sre.label.sender;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.quarkus.sre.label.domain.Order;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;

@ApplicationScoped
@Traced
public class FedexOrderPackageSender {

    @ConfigProperty(name = "fedex.delay", defaultValue = "1000")
    Integer delay;

    @Traced
    public void send(Order order) {
        try {
            // slow 5s
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // ommited
        }
    }

}
