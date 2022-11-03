package top.yueshushu.idem.core.chain;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.idem.core.handle.AbstractLinkedProcessHandle;
import top.yueshushu.idem.core.handle.ProcessHandle;
import top.yueshushu.idem.core.spi.IdemSpiLoader;

import java.util.List;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Slf4j
public class IdemChainPolicy implements HandleChainPolicy {

    @Override
    public ProcessHandleChain build() {
        ProcessHandleChain chain = new ProcessHandleChain();

        List<ProcessHandle> processHandleList = IdemSpiLoader.of(ProcessHandle.class).loadInstanceListSorted();
        processHandleList.forEach(handle -> chain.addLast((AbstractLinkedProcessHandle) handle));

        return chain;
    }
}
