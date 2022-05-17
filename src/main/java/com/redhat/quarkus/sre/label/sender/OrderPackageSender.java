package com.redhat.quarkus.sre.label.sender;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.quarkus.sre.label.domain.Order;

import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.logging.Logger;


@ApplicationScoped
@Traced
public class OrderPackageSender {

    private Random random = new Random();

    @Inject
    FedexOrderPackageSender fedex;

    @Inject
    UPSOrderPackageSender ups;

    @Inject
    Logger logger;

    public void send(Order order) {
        boolean toUps = random.nextBoolean();
        if(toUps) {
            ups.send(order);
        } else {
            fedex.send(order);
        }
    }

}
