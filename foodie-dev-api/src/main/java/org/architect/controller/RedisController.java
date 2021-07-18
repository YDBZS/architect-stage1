package org.architect.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.architect.util.RedisOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试接口
 *
 * @author 多宝
 * @since 2021/3/7 16:34
 */
@Slf4j
@RestController
@RequestMapping("redis")
@Api(value = "Redis测试")
public class RedisController {

    @Resource
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key, String value) {
        redisOperator.set(key, value);
        return "OK";
    }

    @GetMapping("/get")
    public String get(String key) {
        return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public Object hello(String key) {
        redisOperator.del(key);
        return "OJBK";

    }

}
