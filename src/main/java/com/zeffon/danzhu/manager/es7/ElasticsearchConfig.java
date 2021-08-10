package com.zeffon.danzhu.manager.es7;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Create by Zeffon on 2021/3/21
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${spring.es.host}")
    private String host;

    @Value("${spring.es.port}")
    private int port;

    @Value("${spring.es.connTimeout}")
    private int connTimeout;

    @Value("${spring.es.socketTimeout}")
    private int socketTimeout;

    @Value("${spring.es.connectionRequestTimeout}")
    private int connectionRequestTimeout;

    @Bean(destroyMethod = "close", name = "esClient")
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(connTimeout)
                        .setSocketTimeout(socketTimeout)
                        .setConnectionRequestTimeout(connectionRequestTimeout));
        return new RestHighLevelClient(builder);
    }
}

