package com.kgc.kmall.search.config;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shkstart
 * @create 2021-01-04 18:51
 */
@Configuration
public class MyConfig {

//    @Value("{spring.elasticsearch.rest.uris}")
//    private String  elasticsearch;

    @Bean
    public JestClient getJestCline() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://192.168.154.132:9200")
                .multiThreaded(true)
                .build());
        return factory.getObject();
    }

}
