package com.redhat.quarkus.sre.label.sender;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.quarkus.sre.label.domain.Order;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;

import io.micrometer.core.instrument.MeterRegistry;

@ApplicationScoped
@Traced
public class UPSOrderPackageSender {

    @ConfigProperty(name = "ups.delay", defaultValue = "4000")
    Integer delay;


    private io.micrometer.core.instrument.Counter counter;

    UPSOrderPackageSender(MeterRegistry registry) {
        this.counter = registry.counter("sre.label-generator.count.ups");
    }


    public void send(Order order) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // ommited
        }
        counter.increment();
    }

}
