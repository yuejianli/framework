package top.yueshushu.learn.simple.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 简单的一些处理
 *
 * @author yuejianli
 * @date 2022-06-23
 */
@SpringBootTest
@Slf4j
public class SimpleDemo {
	private String connectionString = "127.0.0.1:2181";
	private int sessionTimeOut = 2000;
	private int connectionTimeOut = 3000;
	private CuratorFramework curatorFramework;
	
	/**
	 * 初始化连接
	 */
	@Test
	public void initConnection() {
		//1. 定义重试策略  3000 为超时时长， 10为最大重试次数.
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
		
		// newClient() 方式创建
		CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(connectionString, sessionTimeOut, connectionTimeOut, retryPolicy);
		
		log.info("new Client 方式创建 {}", curatorFramework);
		
		// 2. Builder 方式创建
		CuratorFramework curatorFrameworkBuild = CuratorFrameworkFactory.builder()
				.connectString(connectionString)
				.sessionTimeoutMs(sessionTimeOut)
				.connectionTimeoutMs(connectionTimeOut)
				.retryPolicy(retryPolicy)
				.build();
		log.info(" build() 方式创建 {}", curatorFrameworkBuild);
	}
	
	@Before
	public void initCuratorFramework() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
		curatorFramework = CuratorFrameworkFactory.builder()
				.connectString(connectionString)
				.sessionTimeoutMs(sessionTimeOut)
				.connectionTimeoutMs(connectionTimeOut)
				.retryPolicy(retryPolicy)
				.build();
		// 需要先启动    java.lang.IllegalStateException: Expected state [STARTED] was [LATENT]
		curatorFramework.start();
	}
	
	/**
	 * 创建节点
	 * <p>
	 * .create().forPath("/路径")  会创建普通节点。 但父结点必须存在,
	 * 否则抛出异常: org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /curator/abc/adb
	 * 节点不能重复创建，  org.apache.zookeeper.KeeperException$NodeExistsException: KeeperErrorCode = NodeExists for /curator/abc/adb
	 * .createParentsIfNeeded() 创建父结点，可以不存在
	 */
	@Test
	public void createNodeTest() throws Exception {
//		String curatorPath = curatorFramework.create().forPath("/curator");
//		/*
//		* 创建了节点 /curator
//		* */
//		log.info(">>> 创建了节点 {}",curatorPath);


//		String morePath = curatorFramework.create()
//				 .creatingParentsIfNeeded()
//				.forPath("/curator/abc/adb");
//		log.info(">>>>> 创建多级层节点 {}",morePath);
		
		//
//		curatorFramework.create()
//				.withMode(CreateMode.PERSISTENT_SEQUENTIAL)
//				.forPath("/curator/name");
//		log.info(">>> withMode() 指定 创建节点时，使用的节点方式");

//		curatorFramework.create()
//				.withMode(CreateMode.PERSISTENT_SEQUENTIAL)
//				.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
//				.forPath("/curator/name1");
//		log.info(">>>> withACL() 指定 权限控制");
		
		// 指定编码， GBK, 避免出现乱码的情况。
		curatorFramework.create().forPath("/curator/name2", "岳泽霖".getBytes("GBK"));
		log.info(">>>创建节点时，指定数据");
	}
	
	/**
	 * 获取节点的数据
	 * 不存在的节点，获取数据时，会抛出异常。  org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /curator/noNode
	 */
	@Test
	public void getNodeDataTest() throws Exception {
		byte[] bytes = curatorFramework.getData().forPath("/curator/name2");
		String name = new String(bytes, "GBK");
		// >>>节点 /curator/name2 对应的数据是: 岳泽霖
		log.info(">>>节点 /curator/name2 对应的数据是: {}", name);
		
		byte[] bytes1 = curatorFramework.getData().forPath("/curator/noNode");
		log.info(">>> 不存在的节点 /curator/noNode 获取的数据是: {}", new String(bytes1, "GBK"));
	}
	
	/**
	 * 获取子节点信息
	 */
	@Test
	public void getChildrenNodeTest() throws Exception {
		
		List<String> childNodeList = curatorFramework.getChildren().forPath("/curator");
		if (CollectionUtils.isEmpty(childNodeList)) {
			log.info(">>> 节点 /curator 没有子节点信息");
		}
		log.info(">>> 节点 /curator 有 {} 个子节点", childNodeList.size());
		
		for (String childNode : childNodeList) {
			String path = "/curator/" + childNode;
			String value = new String(curatorFramework.getData().forPath(path), "GBK");
			log.info(">>>>>>>>> 子节点 {},对应的数据是:{}", childNode, value);
		}
	}
	
	/**
	 * 获取节点的状态
	 * getData().storingStatIn ()  获取数据，持久化状态 Stat In 对象
	 */
	@Test
	public void statTest() throws Exception {
		Stat stat = new Stat();
		
		curatorFramework.getData().storingStatIn(stat).forPath("/curator");
		// 节点信息:301,301,1655984736087,1655984736087,0,4,0,0,13,2,325
		log.info(">>> 节点信息:{}", stat);
	}
	
	/**
	 * 节点是否存在
	 */
	@Test
	public void nodeExistTest() throws Exception {
		Stat stat = curatorFramework.checkExists().forPath("/curator");
		log.info("/curator 节点 是否存在: {}", stat == null ? "节点不存在" : "节点存在");
		
		stat = curatorFramework.checkExists().forPath("/curatorNo");
		log.info("/curatorNo 节点 是否存在: {}", stat == null ? "节点不存在" : "节点存在");
	}
	
	/**
	 * 修改数据
	 * setData() 会修改数据
	 */
	@Test
	public void nodeDataChangeTest() throws Exception {
		//修改数据
		String path = "/curator/name2";
		getNodeData(path);
		curatorFramework.setData().forPath(path, "我的笔名是:两个蝴蝶飞".getBytes("GBK"));
		getNodeData(path);
		
		// 也可以根据版本号，进行修改。
		
		Stat stat = new Stat();
		curatorFramework.getData().storingStatIn(stat).forPath(path);
		log.info(">>> 当前的版本号是:{}", stat.getVersion());
		getNodeData(path);
		curatorFramework.setData().withVersion(stat.getVersion()).forPath(path, "我又变了，换回原名:岳建立".getBytes("GBK"));
		getNodeData(path);
	}
	
	public void getNodeData(String nodePath) throws Exception {
		byte[] bytes = curatorFramework.getData().forPath(nodePath);
		String name = new String(bytes, "GBK");
		log.info("{} 当前的数据是:{}", nodePath, name);
	}
	
	/**
	 * 删除节点
	 * delete() 普通删除
	 * delete().deletingChildrenIfNeeded()  强制性多层次删除
	 */
	@Test
	public void deleteNodeTest() throws Exception {
		// 删除节点
		curatorFramework.delete().forPath("/curator/name2");
		
		// 多层次删除
		curatorFramework.delete().deletingChildrenIfNeeded().forPath("/curator/abc");
		
		//guaranteed() 放心， 如果删除不成功，会继续删除。
		curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath("/curator");
	}
	
	/**
	 * 回调函数处理
	 * 回调函数，必须要线程 .sleep() 休眠一下
	 */
	@Test
	public void callBackTest() throws Exception {
		// 创建时回调
//		curatorFramework.create()
//				.inBackground(new BackgroundCallback() {
//					@Override
//					public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
//						log.info(">>> 成功创建节点:{},值是:{}",curatorEvent.getPath(),curatorEvent);
//					}
//				})
//				.forPath("/curator2","创建时回调".getBytes("GBK"));
//
//		TimeUnit.SECONDS.sleep(1);
		
		// 修改节点数据回调
//		curatorFramework.setData()
//				.inBackground((backgroundCallback,curatorEvent)->{
//					log.info(">>> 修改节点数据:{},值是:",curatorEvent.getPath());
//				})
//				.forPath("/curator2","值改变了".getBytes("GBK"));
//
//		TimeUnit.SECONDS.sleep(1);
		
		// 删除节点时 回调
		curatorFramework.delete()
				.inBackground((backgroundCallBack, curatorEvent) -> {
					log.info(">>> 删除节点，删除的路径是:{}", curatorEvent.getPath());
				})
				.forPath("/curator2");
		
		TimeUnit.SECONDS.sleep(1);
	}
}
