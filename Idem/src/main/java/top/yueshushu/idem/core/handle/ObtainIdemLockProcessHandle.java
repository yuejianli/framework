package top.yueshushu.idem.core.handle;

import cn.hutool.core.util.StrUtil;
import top.yueshushu.idem.Constants;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.core.spi.IdemSpi;
import top.yueshushu.idem.exception.IdemEntryException;
import top.yueshushu.idem.exception.IdemNotFindKeyException;
import top.yueshushu.idem.lock.IdemLockUtil;
import top.yueshushu.idem.utils.IdemApplicationContextUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author yjl
 * @date 2022/5/19
 */
@IdemSpi(order = Constants.ORDER_OBTAIN_LOCK_HANDLE)
public class ObtainIdemLockProcessHandle extends AbstractLinkedProcessHandle {

    @Override
    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        String idemKey = (String) context.getIdemKey();
        if (StrUtil.isEmpty(idemKey)) {
            throw new IdemNotFindKeyException("ObtainIdemLockProcessHandle: not find idem key");
        }

        IdemLockUtil lockUtil = IdemApplicationContextUtil.getBean(IdemLockUtil.class);
        IdemRequest idemRequest = (IdemRequest) source;

        boolean success = lockUtil.tryLock(idemKey, idemRequest.getIdemWait(), TimeUnit.SECONDS);
        if (!success) {
            throw new IdemEntryException("ObtainIdemLockProcessHandle: lock failed");
        }

        this.firePreHandle(context, source, idemKey);
    }

    @Override
    public void postHandle(IdemContext context, Object source, Object preResult) {
        this.firePostHandle(context, source, preResult);
    }
}
