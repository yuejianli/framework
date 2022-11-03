package top.yueshushu.idem.core.handle;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import top.yueshushu.idem.Constants;
import top.yueshushu.idem.adapter.IdemAopAspect;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.core.spi.IdemSpi;

import java.lang.reflect.Method;

/**
 * @author yjl
 * @date 2022/6/1
 */
@IdemSpi(order = Constants.ORDER_METHOD_NAME_PARSE_HANDLE)
public class MethodNameParseProcessHandle extends AbstractLinkedProcessHandle implements Parser {
    private static final String prefix = "method-name:";

    @Override
    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        String idemKey = (String) context.getIdemKey();
        String methodName = doParse(source);
        if (StrUtil.isNotEmpty(methodName)) {
            if (StrUtil.isEmpty(idemKey) || StrUtil.endWith(idemKey, ":")) {
                idemKey = StrUtil.concat(true, idemKey, prefix, methodName);
            } else {
                idemKey = StrUtil.concat(true, idemKey, "||", prefix, methodName);
            }
            context.setIdemKey(idemKey);
        }

        this.firePreHandle(context, source, idemKey);
    }

    @Override
    public void postHandle(IdemContext context, Object source, Object preResult) {
        this.firePostHandle(context, source, preResult);
    }

    @Override
    public String doParse(Object source) throws Exception {
        ProceedingJoinPoint pjp = ((IdemRequest) source).getPjp();
        Method originMethod = IdemAopAspect.resolveMethod(pjp);
        return originMethod.getName();
    }
}
