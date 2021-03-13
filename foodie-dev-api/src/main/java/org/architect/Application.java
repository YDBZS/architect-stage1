package org.architect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * SpringBoot启动类
 *
 * @author 多宝
 * @since 2021/3/7 16:31
 */
@SpringBootApplication
// 扫描Mybatis通用的mapper所在的包
@MapperScan(basePackages = {"com.architect.mapper"})
@ComponentScan(basePackages = {"org.n3r.idworker","org.architect"})
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
