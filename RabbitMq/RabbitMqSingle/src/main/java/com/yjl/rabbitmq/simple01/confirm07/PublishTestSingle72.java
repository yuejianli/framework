package com.yjl.rabbitmq.simple01.confirm07;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 批量发布确认
 *
 * @author yuejianli
 * @date 2022-11-22
 */
@Slf4j
public class PublishTestSingle72 {
    public static void main(String[] args) {
        // 发布消息

        Connection connection = ConnectionFactoryUtil.createConnection();
        Channel channel = null;
        try {
            channel = connection.createChannel();

            channel.queueDeclare("YJLF", true, false, false, null);

            TimeInterval timer = DateUtil.timer();

            timer.start();
            // 1. 进行发布确认
            channel.confirmSelect();

            int batchSize = 20;

            int noConfirmCount = 0;

            for (int i = 0; i < 1000; i++) {

                String message = "发布消息" + i;

                channel.basicPublish("", "YJLF", null, message.getBytes(StandardCharsets.UTF_8));


                noConfirmCount++;


                if (noConfirmCount >= batchSize) {
                    // 进行等待确认
                    boolean flag = channel.waitForConfirms();
                    if (flag) {
                        System.out.println(">>>> 发布消息" + batchSize + "条成功");
                    }

                    noConfirmCount = 0;
                }
            }

            if (noConfirmCount > 0) {
                // 进行等待确认
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println(">>>> 发布消息" + noConfirmCount + "条成功");
                }
            }
            // 34 ms
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
