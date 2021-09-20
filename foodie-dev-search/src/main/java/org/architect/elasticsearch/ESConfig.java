package org.architect.elasticsearch;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Netty issue fix
 *
 * @author 多宝
 * @since 2021/9/11 13:38
 */
@Configuration
public class ESConfig {

    @PostConstruct
    public void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

}
