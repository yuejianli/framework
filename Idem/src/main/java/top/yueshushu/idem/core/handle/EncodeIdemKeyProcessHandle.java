package top.yueshushu.idem.core.handle;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.idem.Constants;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.spi.IdemSpi;
import top.yueshushu.idem.exception.IdemEntryException;

/**
 * @author yjl
 * @date 2022/5/25
 */
@IdemSpi(order = Constants.ORDER_ENCODE_IDEM_KEY_HANDLE)
@Slf4j
public class EncodeIdemKeyProcessHandle extends AbstractLinkedProcessHandle {

    private static final String prefix = "idem_";

    @Override
    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        String originIdemKey = (String) context.getIdemKey();
        if (StrUtil.isEmpty(originIdemKey)) {
            throw new IdemEntryException("IdemCacheProcessHandle: not find idem key");
        }

        String usefulIdemKey = prefix + encodeIdemKey(originIdemKey);
        context.setIdemKey(usefulIdemKey);
        log.info("EncodeIdemKeyProcessHandle, origin idemKey:{}, useful idemKey:{}",
                StrUtil.sub(originIdemKey, 0, 200), usefulIdemKey);

        this.firePreHandle(context, source, usefulIdemKey);
    }

    @Override
    public void postHandle(IdemContext context, Object source, Object preResult) {
        this.firePostHandle(context, source, preResult);
    }

    private String encodeIdemKey(String originItemKey) {
        if (StrUtil.length(originItemKey) < 100) {
            return originItemKey;
        }
        return DigestUtil.md5Hex(originItemKey);
    }
}
