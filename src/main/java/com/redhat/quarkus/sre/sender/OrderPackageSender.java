package com.redhat.quarkus.sre.sender;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.quarkus.sre.domain.Order;


@ApplicationScoped
public class OrderPackageSender {

    private Random random = new Random();

    public void send(Order order) {
        boolean ups = random.nextBoolean();

        if(ups) {

        } else {

        }
    }

}
