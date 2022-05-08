package com.udemy.wikimedia.consumer;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URI;

@SpringBootApplication
public class MySpringBootApplication {

  public static void main(String... args) {
    SpringApplication.run(MySpringBootApplication.class, args);
  }


  @Bean
  public RestHighLevelClient openSearchClient() {

    RestHighLevelClient restHighLevelClient;
    //
    final var connString = "http://localhost:9200";
    final var connUri = URI.create(connString);
    //
    final var userInfo = connUri.getUserInfo();
    //

    if (null == userInfo) {
      restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(
              connUri.getHost(), connUri.getPort(), connUri.getScheme())));

    } else {
      var auth = userInfo.split(";");
      //
      var cp = new BasicCredentialsProvider();
      cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));
      restHighLevelClient = new RestHighLevelClient(
              RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
                      .setHttpClientConfigCallback(httpAsync -> httpAsync.setDefaultCredentialsProvider(cp)
                              .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));
    }

    return restHighLevelClient;
  }
}
