package com.udemy.wikimedia;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Component
public class MyProducerRouter extends RouteBuilder {

  @Override
  public void configure() {

    // rest do Camel/Kafka
    // camel-netty-http
    restConfiguration().component("undertow").host("localhost").port(12080);


    from("rest:post:wikimedia")
            //.log("${body}");
            // producer
            .to("kafka:wikimedia.recentchange");

  }

}
