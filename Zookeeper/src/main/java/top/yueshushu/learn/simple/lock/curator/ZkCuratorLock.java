package top.yueshushu.learn.simple.lock.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-21
 */
@Slf4j
public class ZkCuratorLock {
	// 必须以 / 开头
	private final static String ROOT_NODE = "/locks2";
	private String connnectionString = "127.0.0.1:2181";
	private int sessionTimeOut = 2000;
	private int connectionTimeOut = 3000;
	private CuratorFramework curatorFramework;
	private InterProcessLock interProcessLock;
	
	/**
	 * 获取连接
	 */
	public ZkCuratorLock() {
		synchronized (ZkCuratorLock.class) {
			try {
				curatorFramework = getCuratorFramework();
				interProcessLock = new InterProcessMutex(curatorFramework, ROOT_NODE);
			} catch (Exception e) {
				log.error("异常 ", e);
			}
		}
	}
	
	public InterProcessLock getInterProcessLock() {
		return interProcessLock;
	}
	
	/**
	 * 分布式锁初始化
	 */
	private CuratorFramework getCuratorFramework() {
		// 重试策略， 初试时间是 3s, 3次
		RetryPolicy policy = new ExponentialBackoffRetry(3000, 3);
		// 通过工厂创建 Curator
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(connnectionString)
				.connectionTimeoutMs(connectionTimeOut)
				.sessionTimeoutMs(sessionTimeOut)
				.retryPolicy(policy)
				.build();
		// 开启连接
		client.start();
		log.info(">>>> 初始化分布式锁完成");
		return client;
	}
}
