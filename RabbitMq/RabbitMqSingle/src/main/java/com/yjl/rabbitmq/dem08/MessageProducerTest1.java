package com.yjl.rabbitmq.dem08;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

import java.io.IOException;

/**
 * @ClassName:MessageProducerTest3
 * @Description 交换机和队列方法处理。
 * @Author 岳建立
 * @Date 2020/12/20 16:35
 * @Version 1.0
 **/
public class MessageProducerTest1 {
    public static void main(String[] args) {
        //3. 创建连接
        try {
            Connection connection = ConnectionFactoryUtil.createConnection();
            //4. 创建 Channel
            Channel channel = connection.createChannel();
            //5. 创建交换机
            String exchangeName = "RabbitMQ-2";
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, null);
            // 删除交换机
            /**
             * 删除交换机，用 exchangeDelete  传入两个参数， 1是 交换机名称
             * 2.  true 表示交换器未使用时才删除， 若正在使用时则不能删除，抛出异常。
             * false 时 表示必须删除，无论使用不使用。 默认是 false
             */
            //channel.exchangeDelete(exchangeName,false);

            // 创建队列
            String queueName = "queue2";
            channel.queueDeclare(queueName, true, false, false, null);

            /**
             * 删除队列  queueDelete queueDelete queueDelete
             *  传入三个参数:
             *  1. 队列的名称
             *  2. ifUnused  为true时，表示 队列未使用时删除，如果队列正在使用话，那么则不删除，抛出异常。
             *    为false 时，表示必须删除， 默认值为false
             *  3. ifEmpty  为true时，表示队列为空时删除，不为空时则不删除且抛出异常.
             *    为 false 时表示必须删除， 默认值为 false.
             *
             */
            //   channel.queueDelete(queueName,false,false);


            /**
             * 队列创建
             */
            String queueName1 = "queueName1";
            String queueName2 = "queueName2";
            channel.queueDeclare(queueName1, true, false, false, null);
            channel.queueDeclare(queueName2, true, false, false, null);

            /**
             *  队列和交互机进行绑定
             */
            String queueName1Binding = "info";
            String queueName2Binding = "error";
            channel.queueBind(queueName1, exchangeName, queueName1Binding);
            channel.queueBind(queueName2, exchangeName, queueName2Binding);


            /**
             *  发布消息
             */
            String routingKey = "info";
            channel.basicPublish(exchangeName, routingKey, null, "你好啊".getBytes("UTF-8"));


            /**
             * 队列解绑
             */
            channel.queueUnbind(queueName1, exchangeName, queueName1Binding);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
