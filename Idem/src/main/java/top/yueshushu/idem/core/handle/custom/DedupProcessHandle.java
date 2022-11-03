package top.yueshushu.idem.core.handle.custom;

import cn.hutool.core.util.StrUtil;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.core.handle.AbstractLinkedProcessHandle;
import top.yueshushu.idem.exception.IdemDedupException;
import top.yueshushu.idem.exception.IdemNotFindKeyException;
import top.yueshushu.idem.lock.IdemLockUtil;
import top.yueshushu.idem.utils.IdemApplicationContextUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author yjl
 * @date 2022/5/19
 */
public class DedupProcessHandle extends AbstractLinkedProcessHandle {
    @Override
    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        String idemKey = (String) context.getIdemKey();
        if (StrUtil.isEmpty(idemKey)) {
            throw new IdemNotFindKeyException("DedupProcessHandle: not find idem key");
        }

        IdemLockUtil lockUtil = IdemApplicationContextUtil.getBean(IdemLockUtil.class);

        /**
         * 去重的实现
         *
         * 用非等待续命锁，判断是否正在被执行
         * 用setObject缓存的数据，判断是否已被执行
         */
        boolean success = lockUtil.tryLock(idemKey);
        if (!success) {
            throw new IdemDedupException(idemKey);
        }

        boolean isExist = lockUtil.isExistsObject(idemKey);
        if (isExist) {
            throw new IdemDedupException(idemKey);
        }

        this.firePreHandle(context, source, preResult);
    }

    @Override
    public void postHandle(IdemContext context, Object source, Object preResult) {
        this.firePostHandle(context, source, preResult);

        IdemRequest idemRequest = (IdemRequest) source;
        String idemKey = (String) context.getIdemKey();
        if (StrUtil.isNotEmpty(idemKey)) {
            IdemLockUtil lockUtil = IdemApplicationContextUtil.getBean(IdemLockUtil.class);
            lockUtil.setObject(idemKey, "1", idemRequest.getIdemExpired(), TimeUnit.SECONDS);
        }
    }
}
