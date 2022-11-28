package com.yjl.rabbitmq.demo06;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

/**
 * @ClassName:work
 * @Description 消息发布者
 * @Author 岳建立
 * @Date 2020/12/22 19:55
 * @Version 1.0
 * <p>
 * 采用 默认 DefaultConsumer  消费者进行处理展示。
 **/
public class WorkMessageProducer {

    public static final String EXCHANGE_NAME = "work_exchange";

    public static void main(String[] args) throws Exception {
        //3. 创建连接
        Connection connection = ConnectionFactoryUtil.createConnection();
        //4. 创建信道
        Channel channel = connection.createChannel();
        //5. 根据信道去创建交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,
                true, false, null);

        //8.设置消息,进行发布。 轮流发布
        for (int i = 0; i < 10; i++) {
            //通过 basicPublish 方法
            channel.basicPublish(EXCHANGE_NAME, "work_exchange_queue",
                    null, new String("发布第" + i + "条消息,内容为:" + i).getBytes("UTF-8"));
        }
        //9. 关闭
        channel.close();
        connection.close();
    }
}
