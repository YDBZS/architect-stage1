package org.architect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 测试接口
 *
 * @author 多宝
 * @since 2021/3/7 16:34
 */
@ApiIgnore
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return "Hello World!";
    }
}
