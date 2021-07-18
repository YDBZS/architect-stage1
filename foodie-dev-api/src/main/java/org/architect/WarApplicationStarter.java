package org.architect;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 项目部署Jar包启动类失效，配置War包启动类
 * 4、打包war，增加war包的启动类
 *
 * @author 多宝
 * @since 2021/6/6 13:19
 */

public class WarApplicationStarter extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 指向jar包的Application启动类
        return builder.sources(Application.class);
    }
}
