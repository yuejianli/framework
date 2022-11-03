package top.yueshushu.idem.core.chain;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.idem.core.spi.IdemSpiLoader;

import java.util.List;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Slf4j
public class HandleChainProvider {
    private HandleChainProvider() {
    }

    public static ProcessHandleChain newHandleChain(String chainPolicy) {
        ProcessHandleChain chain = null;
        List<HandleChainPolicy> chainPolicyList = IdemSpiLoader.of(HandleChainPolicy.class).loadInstanceList();
        for (HandleChainPolicy handleChainPolicy : chainPolicyList) {
            if (StrUtil.equals(chainPolicy, handleChainPolicy.getClass().getName())) {
                chain = handleChainPolicy.build();
                break;
            }
        }

        return chain;
    }
}
