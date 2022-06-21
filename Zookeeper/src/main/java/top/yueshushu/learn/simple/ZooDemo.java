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

import lombok.extern.slf4j.Slf4j;

/**
 * Zookeeper 简单的使用
 *
 * @author yuejianli
 * @date 2022-06-20
 */
@SpringBootTest
@Slf4j
public class ZooDemo {
	private String connectionString;
	private int sessionTimeOut;
	
	@Before
	public void initData() {
		connectionString = "127.0.0.1:2181";
		sessionTimeOut = 2000;
	}
	
	/**
	 * 简单的连接配置
	 */
	@Test
	public void connectionTest() throws Exception {
		
		ZooKeeper zooKeeper = new ZooKeeper(
				connectionString,
				sessionTimeOut,
				new Watcher() {
					@Override
					public void process(WatchedEvent watchedEvent) {
						log.info(">>>>>获取连接{}", watchedEvent);
					}
				}
		);
		log.info(">>>> 连接信息: {}", zooKeeper);
	}
	
	/**
	 * 查询子节点信息
	 */
	@Test
	public void childrenTest() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(connectionString, sessionTimeOut, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				log.info(">>>>>获取连接{}", event);
			}
		});
		//获取子节点信息
		List<String> children = zooKeeper.getChildren("/", true);
		//  [yjl, zookeeper]
		log.info(">>>>获取子节点名称信息:{}", children);
	}
	
	/**
	 * 获取节点的值
	 */
	@Test
	public void nodeDataTest() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(connectionString, sessionTimeOut, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				log.info(">>>>>连接事件 :{}", event);
			}
		});
		// 是 GBK 类型，注意乱码
		byte[] yjlData = zooKeeper.getData("/yjl", false, null);
		log.info(">>>>获取数据:{}", new String(yjlData, "GBK"));
		
		List<String> children = zooKeeper.getChildren("/", false);
		children.forEach(node -> {
			try {
				byte[] data = zooKeeper.getData("/" + node, false, null);
				log.info(">>>节点 {} 对应的数据是:{}", node, new String(data, "GBK"));
			} catch (Exception e) {
				log.error(">>>获取数据异常:", e);
			}
		});
	}
	
	/**
	 * 创建节点
	 */
	@Test
	public void createNodeTest() throws Exception {
		log.info(">>>>>未创建节点前，查询节点");
		// >获取子节点名称信息:[yjl, zookeeper]
		childrenTest();
		ZooKeeper zooKeeper = new ZooKeeper(connectionString, sessionTimeOut, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				log.info(">>>>>创建连接");
			}
		});
		String s = zooKeeper.create("/java", "Java数据".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		log.info(">>>>创建节点:{}", s);
		log.info(">>>>>创建节点后，查询节点");
		// >>>>获取子节点名称信息:[yjl, java, zookeeper]
		childrenTest();
	}
	
	/**
	 * 删除节点
	 */
	@Test
	public void deleteNodeTest() throws Exception {
		log.info(">>>>>未创建节点前，查询节点");
		// >>>>获取子节点名称信息:[yjl, java, zookeeper]
		childrenTest();
		ZooKeeper zooKeeper = new ZooKeeper(connectionString, sessionTimeOut, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				log.info(">>>>>创建连接");
			}
		});
		zooKeeper.delete("/java", 0);
		log.info(">>>>删除节点:{}");
		log.info(">>>>>创建节点后，查询节点");
		// >获取子节点名称信息:[yjl, zookeeper]
		childrenTest();
	}
	
	/**
	 * 是否存在节点
	 */
	@Test
	public void existNode() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(connectionString, sessionTimeOut, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				log.info(">>>> 连接{}", event);
			}
		});
		
		Stat exists = zooKeeper.exists("/java", false);
		if (null == exists) {
			log.info(">>>> 不存在节点 /java");
		} else {
			log.info(">>>> 存在节点 /java");
		}
		
		Stat exists2 = zooKeeper.exists("/yjl", false);
		if (null == exists2) {
			log.info(">>>> 不存在节点 /yjl");
		} else {
			log.info(">>>> 存在节点 /yjl");
		}
	}
}
