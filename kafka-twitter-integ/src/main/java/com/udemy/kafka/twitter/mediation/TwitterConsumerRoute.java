package com.udemy.kafka.twitter.mediation;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TwitterConsumerRoute extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    from("twitter-search:java")
            .log("Twitter Body - ${body}");
  }
}
