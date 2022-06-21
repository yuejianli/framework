package top.yueshushu.learn.simple.online;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 在线 服务器端
 *
 * @author yuejianli
 * @date 2022-06-20
 */
@Slf4j
public class OnlineServer {
	private String connectionString = "127.0.0.1:2181";
	private int sessionTimeout = 15000;
	private volatile ZooKeeper zooKeeper = null;
	private String PARENT_NODE = "/servers";
	
	public static void main(String[] args) throws Exception {
		OnlineServer onlineServer = new OnlineServer();
		// 创建连接
		onlineServer.getConnection();
		
		TimeUnit.SECONDS.sleep(1);
		
		// 注册在线
		onlineServer.register(args[0]);
		
		// 业务处理
		onlineServer.business(args[0]);
	}
	
	public void getConnection() throws Exception {
		zooKeeper = new ZooKeeper(connectionString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
			
			}
		});
		log.info(">>>> 服务器端连接成功 {}", zooKeeper);
	}
	
	/**
	 * 服务器上线之后，注册
	 */
	public void register(String hostName) throws Exception {
		// 创建临时的，有序 节点。
		String path = zooKeeper.create(PARENT_NODE + "/" + "server", hostName.getBytes(),
				ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		log.info(" 服务器 {} is online ,path is {}", hostName, path);
	}
	
	public void business(String hostName) throws Exception {
		log.info(">>>> {} is working ", hostName);
		TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
	}
}
