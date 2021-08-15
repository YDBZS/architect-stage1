package org.architect.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.architect.util.RedisOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * 大量的key的查询
     *
     * @param keys 未知个数的Redis中的key
     * @return
     */
    @GetMapping("/getAlot")
    public Object getAlot(String... keys) {
        // 循环获取数据是可以实现批量数据的获取的，但是比较写法的low
        // 针对大量的key去做一个循环
//        List<String> result = new ArrayList<>();
//        for (String key : keys) {
//            result.add(redisOperator.get(key));
//        }
        // 通过multiGet的方式批量获取key的值
        return redisOperator.mget(Arrays.asList(keys));
    }

    /**
     * 通过pipeline管道形式批量获取Redis中的数据
     *
     * @param keys 未知个数的Redis中的key
     */
    @GetMapping("/batchGet")
    public Object batchGet(String... keys) {
        return redisOperator.batchGet(Arrays.asList(keys));
    }

}
