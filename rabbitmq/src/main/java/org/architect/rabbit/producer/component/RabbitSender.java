package org.architect.rabbit.producer.component;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * 发送者
 *
 * @author 多宝
 * @since 2022/12/28 17:32
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RabbitSender {

    private final RabbitTemplate rabbitTemplate;

    // 这里就是确认消息的回调监听接口，用于确认消息是否被broker所收到
    final RabbitTemplate.ConfirmCallback confirmCallback =
            (correlationData, ack, cause) -> {
                System.out.println(
                        "消息ACK结果：" + ack + ", correlationData：" + correlationData.getId());
            };

    /**
     * 对外发送消息的方法
     *
     * @param message    具体的消息内容
     * @param properties 额外的附加属性
     * @author 多宝
     * @since 2022/12/28 17:39
     */
    public void send(Object message, Map<String, Object> properties) {
        MessageHeaders headers = new MessageHeaders(properties);
        Message<?> msg =
                MessageBuilder.createMessage(message, headers);

        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 指定业务唯一的ID
        CorrelationData correlationData = new CorrelationData(
                UUID.randomUUID().toString());

        MessagePostProcessor mmp = mm -> {
            System.out.println("---> post to do: " + message);
            return mm;
        };

        rabbitTemplate.convertAndSend("exchange-1", "springboot.rabbit", msg,
                                      mmp, correlationData);
    }

}
