package com.yjl.rabbitmq.simple01.confirm07;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 异步的，进行发布确认
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Slf4j
public class PublishTestSingle73 {
    public static void main(String[] args) {
        // 发布消息

        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = null;
        try {
            channel = connection.createChannel();

            channel.queueDeclare("YJLG", true, false, false, null);

            TimeInterval timer = DateUtil.timer();

            timer.start();

            /**
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目 只要给到序列号
             * 3.支持并发访问
             */

            ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

            ConfirmCallback confirmCallback = (deliveryTag, multiple) -> {

                if (multiple) {
                    // 批量
                    ConcurrentNavigableMap<Long, String> confirmed = concurrentSkipListMap.headMap(deliveryTag, true);
                    // 进行清理。
                    confirmed.clear();
                } else {
                    // 非批量
                    concurrentSkipListMap.remove(deliveryTag);

                }
            };

            ConfirmCallback nConfirmCallback = (deliveryTag, multiple) -> {
                String message = concurrentSkipListMap.get(deliveryTag);
                System.out.println("发布的消息 " + message + "未被确认的 索引号:" + deliveryTag);
            };

            // 注册监听, 一个是 有的，一个是未的。
            /**
             * 添加一个异步确认的监听器
             * 1.确认收到消息的回调
             * 2.未收到消息的回调
             */
            channel.addConfirmListener(confirmCallback, nConfirmCallback);

            for (int i = 0; i < 1000; i++) {
                String message = "发布消息" + i;
                /**
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号
                 * 通过序列号与消息体进行一个关联
                 * 全部都是未确认的消息体
                 */
                concurrentSkipListMap.put(channel.getNextPublishSeqNo(), message);
                channel.basicPublish("", "YJLG", null, message.getBytes());
            }
            //23 ms
            System.out.println(">>>> 共发布 1000 条消息，花费时间:" + timer.intervalMs());

        } catch (Exception e) {
            System.out.println(">>>> 消息出错 " + e.getMessage());
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {

                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {

                }
            }
        }
    }
}
