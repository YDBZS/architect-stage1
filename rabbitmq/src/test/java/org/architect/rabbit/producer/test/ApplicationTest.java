package org.architect.rabbit.producer.test;

import lombok.SneakyThrows;
import org.architect.rabbit.producer.component.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 多宝
 * @since 2022/12/28 18:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    @SneakyThrows
    public void testSender() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("attr1", "12345");
        properties.put("attr2", "abcde");

        rabbitSender.send("hello rabbitmq!", properties);

        TimeUnit.SECONDS.sleep(10);
    }

}
