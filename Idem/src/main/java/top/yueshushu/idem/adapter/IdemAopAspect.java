package top.yueshushu.idem.adapter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.yueshushu.idem.annotation.IdemInspect;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemEntry;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.core.IdemResponse;
import top.yueshushu.idem.exception.IdemCacheWrapper;
import top.yueshushu.idem.exception.IdemNotFindKeyException;
import top.yueshushu.idem.utils.PrintUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Aspect
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IdemAopAspect {
    public IdemAopAspect() {
    }

    @Pointcut("@annotation(top.yueshushu.idem.annotation.IdemInspect)")
    public void idemResourceAnnotationPointcut() {
    }

    public static Method resolveMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Method method = getDeclaredMethodFor(targetClass, signature.getName(), signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        } else {
            return method;
        }
    }

    private static Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException var6) {
            Class<?> superClass = clazz.getSuperclass();
            return superClass != null ? getDeclaredMethodFor(superClass, name, parameterTypes) : null;
        }
    }

    @Around("idemResourceAnnotationPointcut()")
    public Object invokeResourceWithIdem(ProceedingJoinPoint pjp) throws Throwable {
        Method originMethod = IdemAopAspect.resolveMethod(pjp);
        IdemInspect annotation = originMethod.getAnnotation(IdemInspect.class);
        if (annotation == null) {
            throw new IllegalStateException("Wrong state for IdemResource annotation");
        }

        String policy = annotation.policy().getName();
        int waitTime = annotation.waitTimeSeconds();
        int expiredTime = annotation.expiredTimeSeconds();
        Boolean parseURI = annotation.parseHttpURI();
        Boolean parseMethod = annotation.parseHttpMethod();
        String[] headers = annotation.httpHeadersName();
        String[] params = annotation.httpParamsName();
        String[] methodParams = annotation.methodParamsName();
        HttpServletRequest request = RequestContextHolder.getRequestAttributes() != null ?
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest() : null;
        IdemContext idemContext = new IdemContext();
        IdemRequest idemRequest = new IdemRequest(pjp, request, waitTime, expiredTime,
                parseURI, parseMethod, headers, params, methodParams);

        try {

            try {
                IdemEntry.preHandle(policy, idemContext, idemRequest);
            } catch (IdemCacheWrapper cacheWrapper) {
                IdemResponse idemResponse = cacheWrapper.getIdemResponse();
                log.info("return idem cache response: {}", PrintUtil.toJsonString(idemResponse.getProcessResult()));
                return idemResponse.getProcessResult();
            } catch (IdemNotFindKeyException e) {
                log.warn("idem policy not useful for the sense of not find idemKey, but continue process business {}", e.getMessage(), e);
                // do callback, and continue process
            } finally {
                //log.info("preHandle complete");
            }

            Object result = pjp.proceed();
            idemContext.setProceedResult(result);
            return result;
        } catch (Throwable v15) {
            log.debug("happen throwable, and the processResult is discarded");
            //When a throwable occurs, the processResult is discarded.
            throw v15;
        } finally {
            try {
                IdemEntry.postHandle(policy, idemContext, idemRequest);
            } catch (Throwable e) {
                log.warn("postHandle Throwable {}", e.getMessage(), e);
                // do callback
            } finally {
                IdemEntry.finishHandle(policy, idemContext, idemRequest);
            }
        }

    }
}
