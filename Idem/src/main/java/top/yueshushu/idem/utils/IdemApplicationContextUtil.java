package top.yueshushu.idem.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author yjl
 * @date 2022/5/22
 */
public class IdemApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    private static void assertApplicationContext() {
        if (IdemApplicationContextUtil.context == null) {
            throw new RuntimeException("ApplicationContextUtils context is null");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        IdemApplicationContextUtil.context = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        IdemApplicationContextUtil.assertApplicationContext();
        return (T) IdemApplicationContextUtil.context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        IdemApplicationContextUtil.assertApplicationContext();
        return IdemApplicationContextUtil.context.getBean(requiredType);
    }
}
