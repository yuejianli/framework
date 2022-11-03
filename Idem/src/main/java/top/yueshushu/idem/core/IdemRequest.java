package top.yueshushu.idem.core;

import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Data
public final class IdemRequest {
    private ProceedingJoinPoint pjp;
    private HttpServletRequest httpServletRequest;
    private Integer idemWait;
    private Integer idemExpired;
    private Boolean parseURI;
    private Boolean parseMethod;
    private String[] headerList;
    private String[] paramList;

    private String[] methodParamList;

    private IdemRequest() {
    }

    public IdemRequest(ProceedingJoinPoint pjp, HttpServletRequest httpServletRequest,
                       Integer idemWait, Integer idemExpired,
                       Boolean parseURI, Boolean parseMethod,
                       String[] headerList, String[] paramList, String[] methodParamList) {
        this.pjp = pjp;
        this.httpServletRequest = httpServletRequest;
        this.idemWait = idemWait;
        this.idemExpired = idemExpired;
        this.parseURI = parseURI;
        this.parseMethod = parseMethod;
        this.headerList = headerList;
        this.paramList = paramList;
        this.methodParamList = methodParamList;
    }
}
