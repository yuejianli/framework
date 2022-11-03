package top.yueshushu.idem.core;

import top.yueshushu.idem.core.chain.ChainPolicyManager;

/**
 * @author yjl
 * @date 2022/5/19
 */
public final class IdemEntry {
    private static volatile ChainPolicyManager manager = new ChainPolicyManager();

    private IdemEntry() {
    }

    public static void preHandle(String resource, IdemContext context, Object o) throws Throwable {
        manager.preHandle(resource, context, o);
    }

    public static void postHandle(String resource, IdemContext context, Object o) throws Throwable {
        manager.postHandle(resource, context, o);
    }

    public static void finishHandle(String resource, IdemContext context, Object o) {
        context.release();
        context.printCost();
    }
}
