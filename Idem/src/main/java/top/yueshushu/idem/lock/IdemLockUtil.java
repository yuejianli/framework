package top.yueshushu.idem.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author yjl
 * @date 2022/5/20
 */
@Slf4j
public class IdemLockUtil {

    private static final String SPLIT = "=>";
    @Resource(name = "idemRedissonClient")
    private RedissonClient idemRedissonClient;

    private final String lockPrefix;
    private final String cachePrefix;

    public IdemLockUtil(String lockPrefix, String cachePrefix) {
        this.lockPrefix = lockPrefix;
        this.cachePrefix = cachePrefix;
    }

    /**
     * 释放锁
     * key: 锁
     */
    public void unlock(String key) {
        RLock rLock = idemRedissonClient.getLock(lockPrefix + SPLIT + key);
        if (rLock != null && rLock.isHeldByCurrentThread()) {
            rLock.unlock();
        }
    }

    /**
     * 等待锁，必须由创建锁的线程释放，否则，锁一直存在直到程序退出
     * key: 锁
     * waitTime: 等待时间
     * unit：时间单位
     */
    public boolean tryLock(String key, int waitTime, TimeUnit unit) throws InterruptedException {
        RLock rLock = idemRedissonClient.getLock(lockPrefix + SPLIT + key);
        return rLock.tryLock(waitTime, unit);
    }

    /**
     * 续命锁，必须由创建锁的线程释放，否则，锁一直存在直到程序退出
     * key: 锁
     */
    public boolean tryLock(String key) {
        RLock rLock = idemRedissonClient.getLock(lockPrefix + SPLIT + key);
        return rLock.tryLock();
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        return idemRedissonClient.getBucket(cachePrefix + SPLIT + key).get();
    }

    /**
     * 数据是否已存在
     *
     * @param key
     * @return
     */
    public boolean isExistsObject(String key) {
        return idemRedissonClient.getBucket(cachePrefix + SPLIT + key).isExists();
    }

    /**
     * 设置数据
     *
     * @param key
     * @param o
     * @param expiredTime
     */
    public void setObject(String key, Object o, long expiredTime, TimeUnit unit) {
        idemRedissonClient.getBucket(cachePrefix + SPLIT + key).set(o, expiredTime, unit);
    }
}
