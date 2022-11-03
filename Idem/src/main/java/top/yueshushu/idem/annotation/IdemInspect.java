package top.yueshushu.idem.annotation;

import top.yueshushu.idem.core.chain.HandleChainPolicy;
import top.yueshushu.idem.core.chain.IdemChainPolicy;

import java.lang.annotation.*;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface IdemInspect {
    /**
     * 策略
     *
     * @return
     */
    Class<? extends HandleChainPolicy> policy() default IdemChainPolicy.class;

    /**
     * 等待时间
     * <br>
     * 去重策略中该字段不生效
     *
     * @return
     */
    int waitTimeSeconds() default 60;

    /**
     * 过期时间 单位为秒
     *
     * @return
     */
    int expiredTimeSeconds() default 5;

    /**
     * 拼接幂等key，取方法参数名对应值
     * 不填，则默认取全部参数
     *
     * @return
     */
    String[] methodParamsName() default {};


    /**
     * 拼接幂等key，是否解析URI
     *
     * @return
     */
    boolean parseHttpURI() default false;

    /**
     * 拼接幂等key，是否解析Method
     *
     * @return
     */
    boolean parseHttpMethod() default false;

    /**
     * 拼接幂等key，http headers
     *
     * @return
     */
    String[] httpHeadersName() default {};

    /**
     * 拼接幂等key，http parameter
     *
     * @return
     */
    String[] httpParamsName() default {};
}
