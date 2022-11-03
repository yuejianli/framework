package top.yueshushu.idem.core.spi;

import java.lang.annotation.*;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface IdemSpi {

    String value() default "";

    int order() default 0;

    /**
     * Whether create singleton instance
     */
    boolean isSingleton() default true;

    /**
     * Whether is the default Provider
     */
    boolean isDefault() default false;

    int ORDER_HIGHEST = Integer.MIN_VALUE;

    int ORDER_LOWEST = Integer.MAX_VALUE;
}
