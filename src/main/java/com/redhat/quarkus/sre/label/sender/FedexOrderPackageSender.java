package com.redhat.quarkus.sre.label.sender;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.quarkus.sre.label.domain.Order;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@ApplicationScoped
@Traced
public class FedexOrderPackageSender {

    @ConfigProperty(name = "fedex.delay", defaultValue = "1000")
    Integer delay;

    private Counter counter;

    FedexOrderPackageSender(MeterRegistry registry) {
        this.counter = registry.counter("sre.label-generator.count.fedex");
    }

    @Traced
    @Timed(value = "sre_label_generator_tempo_fedex")
    public void send(Order order) {
        try {
            // slow 5s
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // ommited
        }
        counter.increment();
    }

}
