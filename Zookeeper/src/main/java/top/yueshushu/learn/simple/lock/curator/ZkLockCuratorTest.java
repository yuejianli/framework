package top.yueshushu.learn.simple.lock.curator;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-21
 */
@Slf4j
public class ZkLockCuratorTest {
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 10; i++) {
			new Thread(
					new Runnable() {
						@Override
						public void run() {
							try {
								ZkCuratorLock zkCuratorLock = new ZkCuratorLock();
								//获取锁
								InterProcessLock interProcessLock = zkCuratorLock.getInterProcessLock();
								//获取锁
								interProcessLock.acquire();
								log.info(Thread.currentThread().getName() + "第一次获取锁");
								TimeUnit.MILLISECONDS.sleep(1000);
								//获取锁
								interProcessLock.acquire();
								// interProcessLock.acquire(3000,TimeUnit.MILLISECONDS);
								log.info(Thread.currentThread().getName() + "第二次获取锁");
								TimeUnit.MILLISECONDS.sleep(1000);
								//释放锁
								interProcessLock.release();
								log.info(Thread.currentThread().getName() + "释放第二把锁");
								interProcessLock.release();
								log.info(Thread.currentThread().getName() + "释放第一把锁");
							} catch (Exception e) {
							
							}
						}
					}
					, "线程" + i).start();
		}
	}
}
