package org.architect.rabbit.consumer.component;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 接收者
 *
 * @author 多宝
 * @since 2022/12/28 18:04
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RabbitReciever {

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1", durable = "true"),
            exchange = @Exchange(name = "exchange" +
                    "-1",
                                 durable = "true",
                                 type = "topic",
                                 ignoreDeclarationExceptions
                                         = "true"), key =
                    "springboot.*"))
    @SneakyThrows
    public void onMessage(Message<?> message, Channel channel) {
        // 1、收到消息以后进行业务端消费处理
        System.out.println("--------------------------------");
        System.out.println("消费消息：" + message.getPayload());

        // 处理成功之后，获取deliveryTag并进行手工的ACK操作，因为配置文件里面配置的是手工签收
        Long deliveryTag =
                (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

        channel.basicAck(deliveryTag, false);
    }

}
