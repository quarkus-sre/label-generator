package com.redhat.quarkus.sre.sender;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.quarkus.sre.domain.Order;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class FedexOrderPackageSender {

    // 20s
    @ConfigProperty(name = "fedex.delay", defaultValue = "20000")
    Integer delay;

    public void send(Order order) {
        try {
            // slow 5s
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // ommited
        }
    }

}
