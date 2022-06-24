package top.yueshushu.learn.simple.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 事件的监听器处理
 *
 * @author yuejianli
 * @date 2022-06-23
 */
@SpringBootTest
@Slf4j
public class ListenerDemo {
	
	private String connectionStr = "127.0.0.1:2181";
	private int sessionTimeOut = 10000;
	private int connectionTimeOut = 10000;
	private CuratorFramework curatorFramework;
	
	@Before
	public void initFramework() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
		curatorFramework = CuratorFrameworkFactory.builder()
				.connectString(connectionStr)
				.sessionTimeoutMs(sessionTimeOut)
				.connectionTimeoutMs(connectionTimeOut)
				.retryPolicy(retryPolicy)
				.build();
		curatorFramework.start();
	}
	
	/**
	 * 初始化命名空间。 表示底下所有的路径操作，都是在 /curator 下执行的。
	 */
	public void initFrameworkNameSpace() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
		curatorFramework = CuratorFrameworkFactory.builder()
				.connectString(connectionStr)
				.sessionTimeoutMs(sessionTimeOut)
				.connectionTimeoutMs(connectionTimeOut)
				.namespace("/curator")
				.retryPolicy(retryPolicy)
				.build();
		curatorFramework.start();
	}
	
	/**
	 * 此节点的监听
	 * 使用 CuratorCache   .start() 开始启动
	 * <p>
	 * type 为 类型
	 * childData 为原数据
	 * childData1 为 后数据
	 * <p>
	 * 节点创建:
	 * 监听到的类型是: NODE_CREATED
	 * >>>> 监听后路径/curator/name2,数据:岳泽霖
	 * <p>
	 * 节点数据改变:
	 * 监听到的类型是: NODE_CHANGED
	 * >>>> 监听前路径/curator/name2,数据:岳泽霖
	 * >>>> 监听后路径/curator/name2,数据:你好
	 * <p>
	 * 子节点监听:
	 * >>> 监听到的类型是: NODE_CREATED
	 * 监听后路径/curator/name2/acbd,数据:你
	 * <p>
	 * 子节点删除:
	 * rDemo - >>>>> 监听到的类型是: NODE_DELETED
	 * 监听前路径/curator/name2/acbd,数据:你
	 * <p>
	 * 当前节点删除:
	 * tenerDemo - >>>>> 监听到的类型是: NODE_DELETED
	 * >>>> 监听前路径/curator/name2,数据:你好
	 */
	@Test
	public void thisNodeTest() throws Exception {
		// 创建监听的对象 ，监听的是 /curator/name2 节点
		CuratorCache curatorCache = CuratorCache.builder(curatorFramework, "/curator").build();
		curatorCache.listenable().addListener(
				(type, childData, childData1) -> {
					try {
						log.info(">>>>> 当前节点: 监听到的类型是: {}", type);
						if (childData != null) {
							log.info(">>>> 监听前路径{},数据:{}", childData.getPath(), new String(childData.getData(), "GBK"));
						}
						if (childData1 != null) {
							log.info(">>>> 监听后路径{},数据:{}", childData1.getPath(), new String(childData1.getData(), "GBK"));
						}
						
					} catch (Exception e) {
						log.error("有异常", e);
					}
				}
		);
		
		// 监听器需要启动
		curatorCache.start();
		
		//线程休眠
		TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
	}
	
	/**
	 * 监听子节点变化, 自定义监听器
	 * 初始化时:   节点类型:INITIALIZED
	 * 节点对象PathChildrenCacheEvent{type=INITIALIZED, data=null
	 * 添加新节点:   节点类型:CHILD_ADDED
	 * 节点对象PathChildrenCacheEvent{type=CHILD_ADDED, data=ChildData{path='/curator/name/name1', stat=43,43,1656039382076,1656039382076,0,0,0,0,0,0,43
	 * , data=null}}
	 * <p>
	 * 修改新节点数据:   节点类型:CHILD_UPDATED
	 * 节点对象PathChildrenCacheEvent{type=CHILD_UPDATED, data=ChildData{path='/curator/name/name1', stat=43,44,1656039382076,1656039403758,1,0,0,0,6,0,43
	 * , data=[-42, -75, -72, -60, -79, -28]}}
	 * <p>
	 * 删除子节点:   节点类型:CHILD_REMOVED
	 * 节点对象PathChildrenCacheEvent{type=CHILD_REMOVED, data=ChildData{path='/curator/name/name1', stat=43,44,1656039382076,1656039403758,1,0,0,0,6,0,43
	 * , data=[-42, -75, -72, -60, -79, -28]}}
	 */
	@Test
	public void childNodeTest() throws Exception {
		CuratorCache curatorCache = CuratorCache.builder(curatorFramework, "/curator").build();
		// 自定义监听器
		CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder().forPathChildrenCache(
				"/curator/name", curatorFramework,
				(curator, pathChildrenCacheEvent) -> {
					log.info(">>>> 子节点发生了变化");
					log.info("节点类型:{}", pathChildrenCacheEvent.getType());
					log.info("节点对象{}", pathChildrenCacheEvent.toString());
					if (pathChildrenCacheEvent.getData() != null) {
						ChildData childData = pathChildrenCacheEvent.getData();
						
						if (childData.getData() != null) {
							byte[] data = childData.getData();
							log.info(">>>>节点数据是:{}", new String(data, "GBK"));
						}
						
						
					}
				}
		).build();
		//添加监听器
		curatorCache.listenable().addListener(curatorCacheListener);
		// 启动
		curatorCache.start();
		//线程休眠
		TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
	}
	
	/**
	 * 监听节点树
	 * 会将这个节点的 子节点下的数据，都监听到。
	 * 是全的。
	 */
	@Test
	public void treeNodeTest() throws Exception {
		CuratorCache curatorCache = CuratorCache.builder(curatorFramework, "/curator").build();
		//构建监听器
		CuratorCacheListener treeListener = CuratorCacheListener.builder().forTreeCache(
				curatorFramework, (curator, treeCacheEvent) -> {
					log.info(">>>> 树节点发生了变化");
					log.info("节点类型:{}", treeCacheEvent.getType());
					log.info("节点对象{}", treeCacheEvent.toString());
					if (treeCacheEvent.getData() != null) {
						ChildData childData = treeCacheEvent.getData();
						if (childData.getData() != null) {
							byte[] data = childData.getData();
							log.info(">>>>节点数据是:{}", new String(data, "GBK"));
						}
					}
				}
		).build();
		// 添加监听器
		curatorCache.listenable().addListener(treeListener);
		//启动
		curatorCache.start();
		//线程休眠
		TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
	}
	
	
	/**
	 * 当节点发生改变时，对应的 方式是否执行。
	 * <p>
	 * 操作					路径							node  									child								tree
	 * <p>
	 * 最开始，没有 /curator节点								 无	 									INITIALIZED							INITIALIZED
	 * <p>
	 * 创建 /curator 节点		/curator					NODE_CREATED 							CHILD_ADDED							NODE_ADDED
	 * <p>
	 * 修改这个节点的数据									NODE_CHANGED  							CHILD_UPDATED 						NODE_UPDATED
	 * <p>
	 * <p>
	 * 添加子节点				/curator/name				NODE_CREATED						    CHILD_ADDED							NODE_ADDED
	 * <p>
	 * 添加二级子节点			/curator/name/age			无										无									NODE_ADDED
	 */
	@Test
	public void oldBefore5Test() throws Exception {
		
		//----------------- 监听单个节点 -----------------------------------
//1. 创建NodeCache对象
		final NodeCache nodeCache = new NodeCache(curatorFramework, "/curator");
//2. 注册监听
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("节点变化了~");
				
				//获取修改节点后的数据
				byte[] data = nodeCache.getCurrentData().getData();
				System.out.println(new String(data));
			}
		});
//3. 开启监听.如果设置为true，则开启监听是，加载缓冲数据
		nodeCache.start(true);


//----------------- 监听子节点 -----------------------------------
//1.创建监听对象
		PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, "/curator", true);
//2. 绑定监听器
		pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
				System.out.println("子节点变化了~");
				System.out.println(event);
				//监听子节点的数据变更，并且拿到变更后的数据
				//1.获取类型
				PathChildrenCacheEvent.Type type = event.getType();
				//2.判断类型是否是update
				if (type.equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
					System.out.println("数据变了！！！");
					byte[] data = event.getData().getData();
					System.out.println(new String(data));
					
				}
			}
		});
//3. 开启
		pathChildrenCache.start();


//----------------- 监听节点树 -----------------------------------
//1. 创建监听器
		TreeCache treeCache = new TreeCache(curatorFramework, "/curator");
//2. 注册监听
		treeCache.getListenable().addListener(new TreeCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) {
				System.out.println("节点变化了");
				System.out.println(event);
			}
		});
//3. 开启
		treeCache.start();
	}
}
