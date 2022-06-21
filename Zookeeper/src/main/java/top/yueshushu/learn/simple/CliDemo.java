package top.yueshushu.learn.simple;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户端监听
 *
 * @author yuejianli
 * @date 2022-06-20
 */
@SpringBootTest
@Slf4j
public class CliDemo {
	private String connectionString;
	private int sessionTimeout;
	private ZooKeeper zooKeeper = null;
	
	@Before
	public void initZookeeper() throws Exception {
		connectionString = "127.0.0.1:2181";
		sessionTimeout = 2000;
		
		zooKeeper = new ZooKeeper(connectionString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
//
//				None (-1),
//						NodeCreated (1),
//						NodeDeleted (2),
//						NodeDataChanged (3),
//						NodeChildrenChanged (4),
//						DataWatchRemoved (5),
//						ChildWatchRemoved (6);
				log.info(">> 获取监听: {}, 路径是:{}", event.getType(), event.getPath());
				try {
					// 对子节点进行监听
					List<String> children = zooKeeper.getChildren("/java", true);
					for (String nodeName : children) {
						log.info(">>> 获取节点信息:{}", nodeName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Test
	public void createNodeTest() throws Exception {
		String s = zooKeeper.create("/java/createNode", "Java".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT_SEQUENTIAL);
		// 返回路径
		log.info(">>>> 创建节点: {}", s);
	}
	
	@Test
	public void childrenTest() throws Exception {
		List<String> children = zooKeeper.getChildren("/java", true);
		children.forEach(
				node -> {
					log.info(">>>> 获取子节点路径:{}", node);
				}
		);
		// 每当创建新节点后，就会监听到，并且打印出来。
		TimeUnit.SECONDS.sleep(100);
	}
	
	@Test
	public void existNodeTest() throws Exception {
		// 如果存在时，节点的状态
		/**
		 private long czxid;
		 private long mzxid;
		 private long ctime;
		 private long mtime;
		 private int version;
		 private int cversion;
		 private int aversion;
		 private long ephemeralOwner;
		 private int dataLength;
		 private int numChildren;
		 private long pzxid;
		 */
		Stat exists = zooKeeper.exists("/java/createNode", false);
		log.info(">>>节点是否存在 存在 {}", exists == null ? "不存在" : "存在");
	}
}
