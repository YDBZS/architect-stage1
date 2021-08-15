package org.architect.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 *
 * @author 多宝
 * @since 2021/3/13 18:45
 */
@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter() {
        // 1、添加cors配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://127.0.0.1:8080");
        configuration.addAllowedOrigin("http://192.168.52.128:8080");
        configuration.addAllowedOrigin("http://192.168.52.128");
        configuration.addAllowedOrigin("http://www.ttch.com");
        configuration.addAllowedOrigin("http://www.ttch.center.com");
        configuration.addAllowedOrigin("http://www.mtv.com");
        configuration.addAllowedOrigin("http://www.mtv.com:8080");
        configuration.addAllowedOrigin("http://www.music.com");
        configuration.addAllowedOrigin("http://www.music.com:8080");
        configuration.addAllowedOrigin("*");

        // 设置是否发送Cookie信息
        configuration.setAllowCredentials(true);

        //设置读取请求的方式
        configuration.addAllowedMethod("*");

        // 设置允许的Header
        configuration.addAllowedHeader("*");

        // 为url添加映射路径
        UrlBasedCorsConfigurationSource corssource = new UrlBasedCorsConfigurationSource();
        corssource.registerCorsConfiguration("/**", configuration);

        // 3.返回重新定义好的corsSource
        return new CorsFilter(corssource);
    }
}
