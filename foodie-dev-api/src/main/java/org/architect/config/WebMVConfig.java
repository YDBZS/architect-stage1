package org.architect.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置系统接口调用，发起请求，用于RestTemplate
 *
 * @author 多宝
 * @since 2021/5/23 16:54
 */
@Configuration
public class WebMVConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
