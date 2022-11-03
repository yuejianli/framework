package top.yueshushu.idem.core.chain;

import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.handle.AbstractLinkedProcessHandle;

/**
 * @author yjl
 * @date 2022/5/19
 */
public class ProcessHandleChain extends AbstractLinkedProcessHandle {

    /**
     * 头节点
     */
    AbstractLinkedProcessHandle first = new AbstractLinkedProcessHandle() {

        @Override
        public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
            this.firePreHandle(context, source, preResult);
        }

        @Override
        public void postHandle(IdemContext context, Object source, Object preResult) {
            this.firePostHandle(context, source, preResult);
        }
    };

    private AbstractLinkedProcessHandle end = first;

    /**
     * 头插法
     *
     * @param firstHandle
     */
    public void addFirst(AbstractLinkedProcessHandle firstHandle) {
        firstHandle.setNext(first.getNext());
        first.setNext(firstHandle);
        if (end == first) {
            end = firstHandle;
        }
    }

    /**
     * 尾插法
     *
     * @param processHandle
     */
    public void addLast(AbstractLinkedProcessHandle processHandle) {
        end.setNext(processHandle);
        end = processHandle;
    }

    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        if (this.first != null) {
            this.first.firePreHandle(context, source, preResult);
        }
    }

    @Override
    public void postHandle(IdemContext context, Object source, Object preResult) {
        if (this.first != null) {
            this.first.firePostHandle(context, source, preResult);
        }
    }
}
