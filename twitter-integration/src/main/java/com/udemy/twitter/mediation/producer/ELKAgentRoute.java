package com.udemy.twitter.mediation.producer;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@NoArgsConstructor
public class ELKAgentRoute extends RouteBuilder {
  @Override
  public void configure() {


    from("timer://myELKTimer?period=5s&fixedRate=true")
            .transform(constant("Some Payload"))
            .log("${body}")
            // there is a bug in current version from camel
            //.to("elasticsearch-rest://elasticsearch?operation=Index&indexName=twitter")
            .end();

  }
}
