package com.udemy.intro.mediation.producer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@Component
@NoArgsConstructor
public class DemoProducerRoute extends RouteBuilder {


  @Override
  public void configure() {
    //
    final var recordMedataHeader = "org.apache.kafka.clients.producer.RecordMetadata";
    //
    from("timer://basicProducer?period=2s&fixedRate=true")
            .transform(simple("My Message at ${date:now:yyyyMMdd HH:mm:ss}"))
            //.log("${body}")
            //.setHeader(KafkaConstants.KEY, constant("my-key"))
            .to("kafka:my-topic-01")
            /*.process(e -> {
              var recordMetadataList = (List<RecordMetadata>) e.getIn().getHeader(recordMedataHeader);
              if (null != recordMetadataList)
                recordMetadataList.forEach(c -> log.info("Receiving new metadata \n" +
                        "Topic - {}\n " +
                        "Offset: {}\n " +
                        "Timestamp: {}\n" +
                        "Partition: {}\n", c.topic(), c.offset(), c.timestamp(), c.partition()));
            });*/
            .end();

  }
}
