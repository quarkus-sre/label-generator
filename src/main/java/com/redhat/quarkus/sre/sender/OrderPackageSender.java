package com.redhat.quarkus.sre.sender;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.quarkus.sre.domain.Order;


@ApplicationScoped
public class OrderPackageSender {

    private Random random = new Random();

    @Inject
    FedexOrderPackageSender fedex;

    @Inject
    UPSOrderPackageSender ups;

    public void send(Order order) {
        boolean toUps = random.nextBoolean();

        if(toUps) {
            ups.send(order);
        } else {
            fedex.send(order);
        }
    }

}
