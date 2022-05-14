package com.udemy.wikimedia.consumer;


import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Slf4j
@Component
@AllArgsConstructor
public class MyConsumerRouter extends RouteBuilder {
  private final String indexName = "wikimedia";
  private final RestHighLevelClient restHighLevelClient;

  @SneakyThrows
  @PostConstruct
  public void setUpIndex() {
    //try (restHighLevelClient) {
    if (!restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT)) {
      restHighLevelClient.indices().create(new CreateIndexRequest(indexName), RequestOptions.DEFAULT);
    } else {
      log.info("Wikimedia Index already Exists!");
    }
    // }

  }

  @PreDestroy
  @SneakyThrows
  public void cleanUp() {
    restHighLevelClient.close();
  }


  @Override
  public void configure() {

    from("kafka:wikimedia.recentchange?additionalProperties.spring.json.trusted.packages=*")
            //.log("Message received from Kafka : ${body}");
            .process(e -> {
              var body = e.getIn().getBody(String.class);
              //var json = new ObjectMapper().readTree(body);
              //log.info("{}", json.get("payload").get("comment"));
              //try {

              // def and exclusive ID to avoid duplicated Ids on ELK
              /*var id = e.getIn().getHeader("kafka.TOPIC", String.class) +
                      e.getIn().getHeader("kafka.PARTITION", String.class) +
                      e.getIn().getHeader("kafka.OFFSET", String.class);*/

              // get the id from payload body, this is a better approach
              // var id = new ObjectMapper().readTree(body).get("meta").get("id").asText();

              var bulkRequest = new BulkRequest();

              var indexRequest = new IndexRequest(indexName).source(body, XContentType.JSON)/*.id(id)*/;

              // Not recommended
              //var indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

              bulkRequest.add(indexRequest);

              if (bulkRequest.numberOfActions() > 0) {
                var bulkResp = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                log.info("{} record(s)", bulkResp.getItems().length);
              }
              //log.info("{}", indexResponse.getId());
              // }catch (Exception ex) {
              //  ex.printStackTrace();
              //}
              //log.info("{}", json);
            });


  }
}
