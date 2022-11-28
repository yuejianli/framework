package com.yjl.rabbitmq.dem07;

import com.rabbitmq.client.*;
import com.yjl.rabbitmq.util.ConnectionFactoryUtil;

/**
 * @ClassName:MessageConsumer
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/21 15:18
 * @Version 1.0
 **/
public class MessageConsumer {

    private static String exchangeName = "amqp_exchange";

    public static void main(String[] args) throws Exception {
        //3. 创建连接
        Connection connection = ConnectionFactoryUtil.createConnection();
        //4. 根据连接，创建信道 Channel
        Channel channel = connection.createChannel();
        //5. 定义 交换器名称，队列名称，绑定名称，路由名称
        String queueName = "amqp_queue";
        //6. 定义队列
        channel.queueDeclare(queueName, true, false, false, null);
        //7. 通过 basicGet 主动  获取值，是服务器端主动拉取
        /**
         * channel 调用 basicGet() 方法，进行主动获取消息  basicGet()  basicGet() basicGet
         * basicGet(),basicGet() 里面有两个参数
         * 1. 队列的名称， 从哪个队列里面获取值
         * 2. autoAck 是否自动确认。 如果自动应答，打开为true后 RabbitMQ应用送出消息后将立即删除。
         */
        GetResponse getResponse = channel.basicGet(queueName, true);

        if (null == getResponse) {
            System.out.println(">>>> 没有消息");
            return;
        }
        /**
         * GetResponse GetResponse GetResponse GetResponse   获取响应
         *
         * 有四个属性值
         * 1. evelope 包含 deliveryTag, exchange,routingKey 等信息
         * 2. props  BasicProperties 对象，即消息生产时设置该对象特性
         * 3. body 消息体 byte 数组
         * 4. messageCount 消息数量,是剩下的消息数量
         *
         */
        //如果没有消息，则会报错。 并不会主动推送，是主动拉取。
        byte[] bodys = getResponse.getBody();
        System.out.println("获取消息成功:" + new String(bodys));
        System.out.println("获取数目:" + getResponse.getMessageCount());
        //10 关闭
        channel.close();
        connection.close();

    }
}
