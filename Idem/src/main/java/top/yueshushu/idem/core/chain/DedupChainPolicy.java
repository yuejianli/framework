package top.yueshushu.idem.core.chain;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.idem.core.handle.EncodeIdemKeyProcessHandle;
import top.yueshushu.idem.core.handle.HttpParamsParseProcessHandle;
import top.yueshushu.idem.core.handle.MethodNameParseProcessHandle;
import top.yueshushu.idem.core.handle.MethodParamsParseProcessHandle;
import top.yueshushu.idem.core.handle.custom.DedupProcessHandle;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Slf4j
public class DedupChainPolicy implements HandleChainPolicy {

    @Override
    public ProcessHandleChain build() {
        ProcessHandleChain chain = new ProcessHandleChain();

        chain.addLast(new MethodNameParseProcessHandle());
        chain.addLast(new MethodParamsParseProcessHandle());
        chain.addLast(new HttpParamsParseProcessHandle());
        chain.addLast(new EncodeIdemKeyProcessHandle());
        chain.addLast(new DedupProcessHandle());
        return chain;
    }
}
