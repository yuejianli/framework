package top.yueshushu.idem.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import top.yueshushu.idem.adapter.IdemAopAspect;
import top.yueshushu.idem.lock.IdemLockUtil;
import top.yueshushu.idem.utils.IdemApplicationContextUtil;

/**
 * @author yjl
 * @date 2022/5/22
 */
@Configuration
@ConditionalOnProperty(prefix = "idem", name = "enable", havingValue = "true")
@Slf4j
public class IdemAutoConfiguration {

    private static final String LOCK_PREFIX = "idem:lock:";
    private static final String CACHE_PREFIX = "idem:cache:";

    @Value("${idem.isolateGroup}")
    private String isolateGroup;

    @Value("${idem.cache.address}")
    private String redissonAddress;

    @Value("${idem.cache.password}")
    private String redissonPwd;

    @Value("${idem.cache.connectionPoolSize}")
    private Integer connectionPoolSize;

    @Value("${idem.cache.connectionMinimumIdleSize}")
    private Integer connectionMinimumIdleSize;

    @Value("${idem.cache.database}")
    private Integer database;

    @Bean
    public RedissonClient idemRedissonClient() {
        log.info("idemRedissonClient config init");
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(redissonAddress);
        singleServerConfig.setPassword(redissonPwd);
        singleServerConfig.setConnectionPoolSize(connectionPoolSize);
        singleServerConfig.setConnectionMinimumIdleSize(connectionMinimumIdleSize);
        singleServerConfig.setDatabase(database);

        return Redisson.create(config);
    }

    @Bean
    public IdemLockUtil idemLockUtil() {
        return new IdemLockUtil(LOCK_PREFIX + isolateGroup, CACHE_PREFIX + isolateGroup);
    }

    @Bean
    @DependsOn(value = {"idemRedissonClient", "idemLockUtil", "idemApplicationContextUtil"})
    public IdemAopAspect idemAopAspect() {
        return new IdemAopAspect();
    }

    @Bean
    public IdemApplicationContextUtil idemApplicationContextUtil() {
        return new IdemApplicationContextUtil();
    }
}
