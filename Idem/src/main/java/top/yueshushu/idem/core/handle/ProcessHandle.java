package top.yueshushu.idem.core.handle;

import top.yueshushu.idem.core.IdemContext;

/**
 * @author yjl
 * @date 2022/5/19
 */
public interface ProcessHandle {
    void preHandle(IdemContext context, Object source, Object preResult) throws Throwable;

    void firePreHandle(IdemContext context, Object source, Object preResult) throws Throwable;

    void postHandle(IdemContext context, Object source, Object preResult);

    void firePostHandle(IdemContext context, Object source, Object preResult);

}
