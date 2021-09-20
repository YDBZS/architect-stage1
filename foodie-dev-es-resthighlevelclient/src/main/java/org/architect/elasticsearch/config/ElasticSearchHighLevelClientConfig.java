package org.architect.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ElasticSearch高级客户端配置类
 *
 * @author 多宝
 * @since 2021/9/20 14:11
 */
@Configuration
public class ElasticSearchHighLevelClientConfig {

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(
                RestClient.builder(
                    new HttpHost(
                            "192.168.52.112",
                            9200,
                            "http")));
    }

}
