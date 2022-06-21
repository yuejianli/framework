package top.yueshushu.learn.simple.online;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 服务器端，动态上下线
 *
 * @author yuejianli
 * @date 2022-06-20
 */
@Slf4j
public class OnlineClient {
	private static int sessionTimeout = 5000;
	String connectionString = "127.0.0.1:2181";
	private volatile ZooKeeper zooKeeper = null;
	private String PARENT_NODE = "/servers";
	
	public static void main(String[] args) throws Exception {
		OnlineClient onlineClient = new OnlineClient();
		// 创建连接
		onlineClient.getConnection();
		
		TimeUnit.SECONDS.sleep(1);
		// 获取服务器列表 先休眠 1秒钟，保证能够连接上。
		onlineClient.showServersList();
		// 业务处理
		onlineClient.business();
	}
	
	/**
	 * 创建连接
	 */
	public void getConnection() throws Exception {
		System.setProperty("zookeeper.sasl.client", "false");
		zooKeeper = new ZooKeeper(connectionString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				try {
					showServersList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		log.info(">>>> 创建连接成功 {}", zooKeeper);
	}
	
	/**
	 * 获取服务器端列表
	 */
	public void showServersList() throws Exception {
		if (zooKeeper == null) {
			return;
		}
		// 获取列表， 对应的是节点的名称。
		List<String> children = zooKeeper.getChildren(PARENT_NODE, true);
		List<String> hostNameList = new ArrayList<>();
		children.forEach(
				n -> {
					try {
						byte[] data = zooKeeper.getData(PARENT_NODE + "/" + n, false, null);
						hostNameList.add(new String(data, "GBK"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		);
		if (CollectionUtils.isEmpty(hostNameList)) {
			log.info(">>>> 未有服务器上线");
			return;
		}
		log.info(">>> 服务器上线 {}", hostNameList);
	}
	
	/**
	 * 业务处理
	 */
	public void business() throws Exception {
		log.info(">>>> 开始进行业务处理");
		
		TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
	}
	
	
	/**
	 Exception in thread "main" org.apache.zookeeper.KeeperException$ConnectionLossException:
	 KeeperErrorCode = ConnectionLoss for /servers
	 */
	
}
