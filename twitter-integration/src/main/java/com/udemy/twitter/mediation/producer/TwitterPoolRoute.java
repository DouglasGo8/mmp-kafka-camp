package com.udemy.twitter.mediation.producer;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author dougb
 */
@Component
@NoArgsConstructor
public class TwitterPoolRoute extends RouteBuilder {

  @Override
  public void configure() {

    onException(Exception.class)
            .log("${body} fail");

    /*from("twitter-search://" + this.rangeOfTerms() + "?repeatCount=10&count=1")
            //.marshal().json(JsonLibrary.Jackson)
            .to("kafka:twitter_tweets")
            //.log("${body}")
            .end();*/
            //.log("Twitter Body - ${body}");


    /*from("timer://myTimerSearch?period=5s&fixedRate=true")
            .transform(method(TwitterConsumerRoute.class, "rangeOfTerms"))
            //.log("${body}")
            .toD("twitter-search:${body}")
            .marshal().json(JsonLibrary.Jackson)
            .log("${body}")
            .end();*/

    /*from("timer://myTimer?period=5s&fixedRate=true")
            .transform(method(TwitterConsumerRoute.class, "rangeOfTerms"))
            .log("${body}")
            .end();*/
  }


  /**
   * When Camel in Consumer mode, just one word will be selected (singleton) in each boot
   * @return a text/term to Twitter Search
   */
  String rangeOfTerms() {
    //
    var terms = new String[]{"java", "python", "ukraine", "javascript", "apache camel", "kafka"};
    return terms[ThreadLocalRandom.current().nextInt(0, terms.length - 1)];
  }
}
