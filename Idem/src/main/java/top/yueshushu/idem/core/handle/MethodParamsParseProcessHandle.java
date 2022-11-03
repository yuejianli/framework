package top.yueshushu.idem.core.handle;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import top.yueshushu.idem.Constants;
import top.yueshushu.idem.adapter.IdemAopAspect;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.core.spi.IdemSpi;
import top.yueshushu.idem.utils.CommonUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;

/**
 * @author yjl
 * @date 2022/5/19
 */
@IdemSpi(order = Constants.ORDER_METHOD_PARAMS_PARSE_HANDLE)
@Data
public class MethodParamsParseProcessHandle extends AbstractLinkedProcessHandle implements Parser {

    private static final String prefix = "method-params:";

    @Override
    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        String idemKey = (String) context.getIdemKey();
        String methodParamsName = doParse(source);
        if (StrUtil.isNotEmpty(methodParamsName)) {
            if (StrUtil.isEmpty(idemKey) || StrUtil.endWith(idemKey, ":")) {
                idemKey = StrUtil.concat(true, idemKey, prefix, methodParamsName);
            } else {
                idemKey = StrUtil.concat(true, idemKey, "||", prefix, methodParamsName);
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
        String[] methodParamList = ((IdemRequest) source).getMethodParamList();
        Method originMethod = IdemAopAspect.resolveMethod(pjp);
        return getUniqueIdForMethod(pjp, originMethod, methodParamList);
    }

    private String getUniqueIdForMethod(ProceedingJoinPoint pjp, Method originMethod, String[] methodParamList) {
        //没有指定方法参数，则默认全取
        if (ArrayUtil.isEmpty(methodParamList)) {
            StringBuilder uniqueBuilder = new StringBuilder();
            Parameter[] parameters = originMethod.getParameters();
            if (!ArrayUtil.isEmpty(parameters)) {
                for (int i = 0; i < parameters.length; i++) {
                    CommonUtil.addPrefix(uniqueBuilder);
                    uniqueBuilder.append(JSON.toJSONString(pjp.getArgs()[i]));
                }
            }

            return uniqueBuilder.toString();
        }

        //否则，根据指定方法参数取值
        HashSet<String> methodSet = CollectionUtil.newHashSet(methodParamList);
        StringBuilder uniqueBuilder = new StringBuilder();
        Parameter[] parameters = originMethod.getParameters();
        if (!ArrayUtil.isEmpty(parameters)) {
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (methodSet.contains(parameter.getName())) {
                    CommonUtil.addPrefix(uniqueBuilder);
                    uniqueBuilder.append(JSON.toJSONString(pjp.getArgs()[i]));
                }
            }
        }

        return uniqueBuilder.toString();
    }
}
