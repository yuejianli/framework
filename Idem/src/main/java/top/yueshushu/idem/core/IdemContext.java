package top.yueshushu.idem.core;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.idem.exception.IdemEntryException;
import top.yueshushu.idem.lock.IdemLockUtil;
import top.yueshushu.idem.utils.IdemApplicationContextUtil;

/**
 * @author yjl
 * @date 2022/5/23
 */
@Data
@Slf4j
public final class IdemContext {
    private Long startTime;               //开始时间
    private Object idemKey;               //锁Key
    private IdemResponse idemResponse;    //业务执行结果包装

    public IdemContext() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean checkInspectParam(IdemRequest idemRequest) {
        if (idemRequest.getIdemWait() < 0) {
            throw new IdemEntryException("check request idemWait[" + idemRequest.getIdemWait() + "] less than 0");
        }

        if (idemRequest.getIdemExpired() < 0) {
            throw new IdemEntryException("check request idemExpired[" + idemRequest.getIdemExpired() + "] less than 0");
        }
        return true;
    }

    public void setProceedResult(Object proceedResult) {
        IdemResponse idemResponse = new IdemResponse();
        idemResponse.setProcessResult(proceedResult);
        this.idemResponse = idemResponse;
    }

    /**
     * 清除占用资源
     * 清除redis锁
     */
    public void release() {
        if (!ObjectUtil.isEmpty(idemKey)) {
            IdemLockUtil lockUtil = IdemApplicationContextUtil.getBean(IdemLockUtil.class);
            lockUtil.unlock((String) idemKey);
        }
    }

    /**
     * 打印整个流程耗时
     */
    public void printCost() {
        Long cost = System.currentTimeMillis() - startTime;
        log.debug("idemKey[{}], the entire procedure cost:{}", idemKey, cost);
    }
}
