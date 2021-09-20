package org.architect.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * ES项目启动类
 *
 * @author 多宝
 * @since 2021/9/20 14:08
 */
// 移除Redis自动装配，因为是Redis的pom配置在父工程，所以当前模块如果不配置redis信息，就会导致项目启动不了
@SpringBootApplication(exclude = {RedisAutoConfiguration.class, SecurityAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
