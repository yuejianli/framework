package top.yueshushu.idem.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author yjl
 * @date 2022/5/11
 */
@Slf4j
public final class IdemHelper {
    public static final String IDEM_KEY = "x-request-idem-token";

    private IdemHelper() {
    }

    public static String createIdemToken() {
        return UUID.randomUUID().toString();
    }

    public static String obtainIdemToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(IDEM_KEY);
    }
}
