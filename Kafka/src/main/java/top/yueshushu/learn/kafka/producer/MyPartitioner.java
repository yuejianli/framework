package top.yueshushu.learn.kafka.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-27
 */
public class MyPartitioner implements Partitioner {
	
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		String kafkaValue = value.toString();
		if (kafkaValue.startsWith("A")) {
			return 0;
		} else if (kafkaValue.startsWith("B")) {
			return 1;
		} else {
			return 2;
		}
	}
	
	@Override
	public void close() {
	
	}
	
	@Override
	public void configure(Map<String, ?> configs) {
	
	}
}
