package com.redhat.quarkus.sre.sender;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.quarkus.sre.domain.Order;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class UPSOrderPackageSender {

    // 5s
    @ConfigProperty(name = "ups.delay", defaultValue = "5000")
    Integer delay;

    public void send(Order order) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // ommited
        }
    }

}
