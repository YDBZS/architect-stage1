package org.architect.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置系统接口调用，发起请求，用于RestTemplate
 *
 * @author 多宝
 * @since 2021/5/23 16:54
 */
@Configuration
public class WebMVConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    // 实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")                         // 映射Swagger2
                .addResourceLocations("file:"+"/E:/mayuncode/aichitect-stage1/userface/");       // 映射本地静态资源
    }
}
