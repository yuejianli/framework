package top.yueshushu.idem.core.handle.custom;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.core.handle.AbstractLinkedProcessHandle;
import top.yueshushu.idem.core.spi.IdemSpi;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Slf4j
@IdemSpi(order = -8500)
public class LogPrintProcessHandle extends AbstractLinkedProcessHandle {
    @Override
    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        IdemRequest idemRequest = (IdemRequest) source;
        log.debug("LogPrintProcessHandle preHandle, idemKey:{}, waitTime:{}, expiredTime:{}",
                context.getIdemKey(), idemRequest.getIdemWait(), idemRequest.getIdemExpired());

        this.firePreHandle(context, source, preResult);
    }

    @Override
    public void postHandle(IdemContext context, Object source, Object preResult) {
        IdemRequest idemRequest = (IdemRequest) source;
        log.debug("LogPrintProcessHandle postHandle, idemKey:{}, waitTime:{}, expiredTime:{}, isCacheResult:{}",
                context.getIdemKey(), idemRequest.getIdemWait(), idemRequest.getIdemExpired(),
                !ObjectUtil.isEmpty(context.getIdemResponse()));

        this.firePostHandle(context, source, preResult);
    }
}
