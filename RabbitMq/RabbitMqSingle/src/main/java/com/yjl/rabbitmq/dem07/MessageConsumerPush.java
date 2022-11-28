package com.yjl.rabbitmq.dem07;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;

/**
 * @ClassName:MessageConsumer
 * @Description 消息消费者
 * @Author 岳建立
 * @Date 2020/12/21 15:18
 * @Version 1.0
 **/
public class MessageConsumerPush {
    public static void main(String[] args) throws Exception {
        //3. 创建连接
        Connection connection = ConnectionFactoryUtil.createConnection();
        //4. 根据连接，创建信道 Channel
        final Channel channel = connection.createChannel();
        //5. 定义 交换器名称，队列名称，绑定名称，路由名称
        String queueName = "amqp_queue";
        //6. 定义队列
        channel.queueDeclare(queueName, true, false, false, null);
        //7. 定义 DefaultConsumer
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            /**
             *
             * 消息处理逻辑
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(">>>>>>>>>>我正在消费消息");
                System.out.println("当前消息者的编号是:" + consumerTag + ",消费的内容是:" +
                        new String(body, "UTF-8"));
                /**
                 * basicRecover basicRecover basicRecover basicRecover 消费重发
                 * 只有一个参数
                 * true: 表示可以将重发消息发送给其他消费者
                 *  false: 只能将消息发送给相同的消费者。
                 *  默认是true
                 */
                channel.basicRecover(true);
            }

            /**
             * 开始消费前执行
             * @param consumerTag
             */
            @Override
            public void handleConsumeOk(String consumerTag) {
                System.out.println(">>>>>>>>>>>>>>>>我要开始消费了");
            }

            /**
             * 当连接 Connection /Channnel 断开关闭时执行一次
             */
            @Override
            public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
                System.out.println(">>>>>>>>>>>>我要断开连接了");
            }

            /**
             * 当未确认的消息，重新被执行时
             * @param consumerTag
             */
            @Override
            public void handleRecoverOk(String consumerTag) {
                System.out.println(">>>>>>>>>>>>>>>>重新执行消息,即消息重发");
            }

            /**
             * 当消费者被取消时，执行这个方法
             * @param consumerTag
             */
            @Override
            public void handleCancelOk(String consumerTag) {
                System.out.println(">>>>>>>>>>>>>>>>>>消费者被取消了");
            }
        };
        System.out.println(">>>>>>>>>>>>>正在等待接收消息....");
        //9. 通过 basicConsume 方法进行推送获取
        channel.basicConsume(queueName, true, "conusmerTag2", defaultConsumer);

        /**
         * 两个消费者关联一个队列时，会一个消费者一个消费者进行处理。
         */
        System.in.read();
        //10 关闭
        channel.close();
        connection.close();

    }
}
