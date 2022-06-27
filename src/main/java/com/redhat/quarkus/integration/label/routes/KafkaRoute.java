/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.quarkus.integration.label.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class KafkaRoute extends RouteBuilder {

  @ConfigProperty(name = "ups.service.base.url")
  String upsServiceBaseURL;

  @ConfigProperty(name = "ups.service.base.endpoint")
  String upsServiceBaseEndpoint;

  @ConfigProperty(name = "fedex.service.base.url")
  String fedexServiceBaseURL;

  @ConfigProperty(name = "fedex.service.base.endpoint")
  String fedexServiceBaseEndpoint;

  @Override
  public void configure() throws Exception {

    from("kafka:{{kafka.topic.name.orders.in}}?consumersCount=20&={{camel.component.kafka.configuration.group-id}}")
        .routeId("kafka-in-route")
        .log("Received Kafka: \"${body}\"")
        .threads(20)
        .choice()
          // Ugly. but works!
          .when(body().contains("ups"))
              .to("direct:upsTimerRequest")
          .otherwise()
              .to("direct:fedexTimerRequest")
          .end()
        .log("Received Rest: \"${body}\"");
       
       
        from("direct:upsTimerRequest")
            .to("micrometer:timer:upsTimerRequest?action=start")
            .toD("http://" + upsServiceBaseURL + upsServiceBaseEndpoint)
            .to("micrometer:timer:upsTimerRequest?action=stop")
            .to("micrometer:counter:upsRequestCounter");
           
         from("direct:fedexTimerRequest")
            .to("micrometer:timer:fedexTimerRequest?action=start")
            .toD("http://" + fedexServiceBaseURL + fedexServiceBaseEndpoint)
            .to("micrometer:timer:fedexTimerRequest?action=stop")
            .to("micrometer:counter:fedexRequestCounter");

  }
}