package com.udemy.kafka.intro.mediation.consumer;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@NoArgsConstructor
public class DemoConsumerRouter extends RouteBuilder {
  @Override
  public void configure() {

    from("kafka:my-topic-01")
            .log("Message received from Kafka : ${body}")
            .log("    on the topic ${headers[kafka.TOPIC]}")
            .log("    on the partition ${headers[kafka.PARTITION]}")
            .log("    with the offset ${headers[kafka.OFFSET]}")
            .log("    with the key ${headers[kafka.KEY]}");

  }
}
