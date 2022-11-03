package top.yueshushu.idem.core.chain;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.idem.Constants;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.exception.IdemSpiLoaderException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Slf4j
public class ChainPolicyManager {
    private static volatile Map<String, ProcessHandleChain> chainMap = new HashMap<>();

    private static final Object LOCK = new Object();

    public void preHandle(String chainName, IdemContext context, Object source) throws Throwable {
        context.checkInspectParam((IdemRequest) source);

        //find process chain
        ProcessHandleChain chain = lookProcessChain(chainName);
        if (chain == null) {
            throw new IdemSpiLoaderException("look chain failed");
        }

        chain.preHandle(context, source, null);
    }

    private ProcessHandleChain lookProcessChain(String chainName) {
        ProcessHandleChain chain = chainMap.get(chainName);
        if (chain == null) {
            synchronized (LOCK) {
                chain = chainMap.get(chainName);
                if (chain == null) {
                    //size limit
                    if (chainMap.size() >= Constants.MAX_CHAIN_LENGTH) {
                        return null;
                    }

                    chain = HandleChainProvider.newHandleChain(chainName);
                    Map<String, ProcessHandleChain> newMap = new HashMap<>(chainMap.size() + 1);
                    newMap.putAll(chainMap);
                    newMap.put(chainName, chain);
                    chainMap = newMap;
                }
            }
        }
        return chain;
    }

    public void postHandle(String chainName, IdemContext context, Object source) throws Throwable {
        //find process chain
        ProcessHandleChain chain = lookProcessChain(chainName);
        if (chain == null) {
            throw new IdemSpiLoaderException("look chain failed");
        }

        chain.postHandle(context, source, null);
    }

}
