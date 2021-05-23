package org.architect.config;

import lombok.extern.slf4j.Slf4j;
import org.architect.service.OrdersService;
import org.architect.util.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 查询订单状态，定时关闭超时未支付订单
 *
 * @author 多宝
 * @since 2021/5/23 19:18
 */
@Slf4j
@Component
public class OrderJob {

    @Resource
    private OrdersService ordersService;

    /**
     * 使用定时任务关闭超期未支付订单，会存在的弊端
     * 1、会有时间差，程序不严谨
     *      10:39下单，11:00检查不足一小时，12:00检查，超过一小时多余39分钟
     * 2、不支持集群
     *      单机没毛病，使用集群后，就会有多个定时任务
     *      解决方案：只是用一台计算机节点，单独用来运行所有的定时任务
     * 3、会对数据库全表搜索，及其影响数据库性能    select * from order where orderStatus = 10;
     * 定时任务，仅仅只适用于小型轻量级应用项目，传统项目
     *
     * 后续课程会涉及到消息队列：MQ -> RabbitMQ,RocketMQ,Kafka,ZeroMQ
     *  延时任务 (队列)
     *  10:12下单的，未付款(10)状态，11:12分检查，如果当前状态还是10，则直接关闭订单即可
     *
     */

//    @Scheduled(cron = "0/3 * * * * ?")    每隔三秒执行一次
    @Scheduled(cron = "0 0 0/1 * * ?")    // 每隔一小时执行一次
    public void autoCloseOrder() {
        ordersService.closeOrder();
        log.info("执行定时任务，当前的时间是：" + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }

}
