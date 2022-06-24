package top.yueshushu.learn.simple.lock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-21
 */
@Slf4j
public class ZkLock {
	private static final String ROOT_NODE = "locks";
	private ZooKeeper zooKeeper;
	private String currentNode;
	private String waitNode;
	
	private CountDownLatch waitCountDownLatch = new CountDownLatch(1);
	
	/**
	 * 获取连接
	 */
	public ZkLock() {
		synchronized (ZkLock.class) {
			try {
				String connnectionString = "127.0.0.1:2181";
				int sessionTimeOut = 2000;
				zooKeeper = new ZooKeeper(
						connnectionString, sessionTimeOut, event -> {
					if (event.getType() == Watcher.Event.EventType.NodeDeleted && event.getPath().equals(waitNode)) {
						waitCountDownLatch.countDown();
					}
				}
				);
				Stat stat = zooKeeper.exists("/" + ROOT_NODE, false);
				if (stat == null) {
					//没有这个节点，就新创建
					log.info(">>> 没有节点 /" + ROOT_NODE + ",新创建");
					zooKeeper.create("/" + ROOT_NODE, "lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
							CreateMode.PERSISTENT);
				}
				log.info("zookeeper --> {}", zooKeeper);
			} catch (Exception e) {
				log.error("异常 ", e);
			}
		}
	}
	
	/**
	 * 加锁
	 */
	public void lock() throws Exception {
		//加锁
		String currentThreadName = Thread.currentThread().getName();
		if (tryLock()) {
			log.info(">>>>线程 {}试图获取锁成功", currentThreadName);
		} else {
			log.info(">>>>> 线程 {} 未获取到锁,进入等待", currentThreadName);
			waitLock();
			lock();
		}
	}
	
	/**
	 * 等待获取锁
	 */
	private void waitLock() throws Exception {
		if (StringUtils.isEmpty(waitNode)) {
			return;
		}
		// 监听上一个节点
		if (zooKeeper.exists(waitNode, false) == null) {
			log.error(">>> 上一个节点 {} 不存在了", waitNode);
			return;
		}
		zooKeeper.getData(waitNode, true, new Stat());
		waitCountDownLatch.await();
	}
	
	/**
	 * 试图获取锁
	 */
	private boolean tryLock() throws Exception {
		if (StringUtils.isEmpty(currentNode)) {
			String LOCK_NODE = "seq-";
			currentNode = zooKeeper.create("/" + ROOT_NODE + "/" + LOCK_NODE, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
		}
		TimeUnit.MILLISECONDS.sleep(10);
		
		// 看共有几个节点
		List<String> children = zooKeeper.getChildren("/" + ROOT_NODE, false);
		if (CollectionUtils.isEmpty(children) || children.size() == 1) {
			return true;
		}
		List<String> childrenList = children.stream().map(n -> ("/" + ROOT_NODE + "/") + n).sorted().collect(Collectors.toList());
		// 对 数据进行处理
		
		// 看当前的节点，有几个.
		int index = childrenList.indexOf(currentNode);
		if (index == -1) {
			log.error("未查询出数据,数据异常");
			return false;
		} else if (index == 0) {
			return true;
		} else {
			// 查询出前面的数据
			waitNode = childrenList.get(index - 1);
			return false;
		}
	}
	
	/**
	 * 解锁
	 */
	public void unlock() throws Exception {
		if (StringUtils.isEmpty(currentNode)) {
			log.error(">>> 已经删除了节点 {}", currentNode);
			return;
		}
		String currentThreadName = Thread.currentThread().getName();
		
		//指定节点版本号，不加也可以，输入-1表示版本号不参与
		zooKeeper.delete(currentNode, -1);
		log.info(">>> 线程 {} 释放了锁资源 {}", currentThreadName, currentNode);
	}
}
