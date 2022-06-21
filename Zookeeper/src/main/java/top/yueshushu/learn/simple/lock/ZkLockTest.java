package top.yueshushu.learn.simple.lock;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-21
 */
@Slf4j
public class ZkLockTest {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(
					new Runnable() {
						@Override
						public void run() {
							log.info(Thread.currentThread().getName() + "开始运行");
							try {
								ZkLock zkLock1 = new ZkLock();
								zkLock1.lock();
								log.info(Thread.currentThread().getName() + "获取锁");
								TimeUnit.MILLISECONDS.sleep(1000);
								zkLock1.unlock();
								log.info(Thread.currentThread().getName() + "释放锁");
							} catch (Exception e) {
							
							}
						}
					}
					, "线程" + i).start();
		}
	}
}
