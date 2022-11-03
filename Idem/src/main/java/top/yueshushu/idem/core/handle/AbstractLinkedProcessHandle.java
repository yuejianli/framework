package top.yueshushu.idem.core.handle;

import top.yueshushu.idem.core.IdemContext;

/**
 * @author yjl
 * @date 2022/5/19
 */
public abstract class AbstractLinkedProcessHandle implements ProcessHandle {

    private AbstractLinkedProcessHandle next = null;

    @Override
    public void firePreHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        if (this.next != null) {
            this.next.preHandle(context, source, preResult);
        }
    }

    @Override
    public void firePostHandle(IdemContext context, Object source, Object preResult) {
        if (this.next != null) {
            this.next.postHandle(context, source, preResult);
        }
    }

    public AbstractLinkedProcessHandle getNext() {
        return next;
    }

    public void setNext(AbstractLinkedProcessHandle next) {
        this.next = next;
    }
}
