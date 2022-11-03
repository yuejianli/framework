package top.yueshushu.idem.core.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.idem.Constants;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.core.IdemResponse;
import top.yueshushu.idem.core.spi.IdemSpi;
import top.yueshushu.idem.exception.IdemCacheWrapper;
import top.yueshushu.idem.exception.IdemEntryException;
import top.yueshushu.idem.lock.IdemLockUtil;
import top.yueshushu.idem.utils.IdemApplicationContextUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author yjl
 * @date 2022/5/25
 */
@IdemSpi(order = Constants.ORDER_IDEM_CACHE_HANDLE)
@Slf4j
public class IdemCacheProcessHandle extends AbstractLinkedProcessHandle {
    @Override
    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        String idemKey = (String) context.getIdemKey();
        if (StrUtil.isEmpty(idemKey)) {
            throw new IdemEntryException("IdemCacheProcessHandle: not find idem key");
        }

        IdemLockUtil lockUtil = IdemApplicationContextUtil.getBean(IdemLockUtil.class);
        IdemResponse idemResponse = (IdemResponse) lockUtil.getObject(idemKey);
        if (!ObjectUtil.isEmpty(idemResponse)) {
            throw new IdemCacheWrapper(idemResponse);
        }

        this.firePreHandle(context, source, idemKey);
    }

    @Override
    public void postHandle(IdemContext context, Object source, Object preResult) {
        IdemLockUtil lockUtil = IdemApplicationContextUtil.getBean(IdemLockUtil.class);
        IdemRequest idemRequest = (IdemRequest) source;
        Object idemResponse = context.getIdemResponse();
        String idemKey = (String) context.getIdemKey();

        if (!ObjectUtil.isEmpty(idemResponse) && StrUtil.isNotEmpty(idemKey)) {
            log.debug("IdemCacheProcessHandle: idemKey:{}, idemResponse:{}", idemKey, idemResponse);
            lockUtil.setObject(idemKey, idemResponse, idemRequest.getIdemExpired(), TimeUnit.SECONDS);
        }

        this.firePostHandle(context, source, preResult);
    }
}
