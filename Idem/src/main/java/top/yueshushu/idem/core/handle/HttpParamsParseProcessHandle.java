package top.yueshushu.idem.core.handle;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import top.yueshushu.idem.Constants;
import top.yueshushu.idem.core.IdemContext;
import top.yueshushu.idem.core.IdemRequest;
import top.yueshushu.idem.core.spi.IdemSpi;
import top.yueshushu.idem.utils.CommonUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yjl
 * @date 2022/5/19
 */
@IdemSpi(order = Constants.ORDER_HTTP_PARAMS_PARSE_HANDLE)
public class HttpParamsParseProcessHandle extends AbstractLinkedProcessHandle implements Parser {

    private static final String prefix = "http:";

    @Override
    public void preHandle(IdemContext context, Object source, Object preResult) throws Throwable {
        String idemKey = (String) context.getIdemKey();
        String httpStr = doParse(source);
        if (StrUtil.isNotEmpty(httpStr)) {
            if (StrUtil.isEmpty(idemKey) || StrUtil.endWith(idemKey, ":")) {
                idemKey = StrUtil.concat(true, idemKey, prefix, httpStr);
            } else {
                idemKey = StrUtil.concat(true, idemKey, "||", prefix, httpStr);
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
        IdemRequest idemRequest = (IdemRequest) source;
        HttpServletRequest servletRequest = idemRequest.getHttpServletRequest();

        StringBuilder result = new StringBuilder();
        if (CompareUtil.compare(idemRequest.getParseURI(), true) == 0
                && !ObjectUtil.isEmpty(servletRequest)) {
            result.append(servletRequest.getRequestURI());
        }

        if (CompareUtil.compare(idemRequest.getParseMethod(), true) == 0
                && !ObjectUtil.isEmpty(servletRequest)) {
            CommonUtil.addPrefix(result);
            result.append(servletRequest.getMethod());
        }

        String[] headersList = idemRequest.getHeaderList();
        if (isCanEnter(headersList, servletRequest)) {
            for (String header : headersList) {
                String hStr = servletRequest.getParameter(header);
                if (!StrUtil.isEmpty(hStr)) {
                    CommonUtil.addPrefix(result);
                    result.append(hStr);
                }
            }
        }

        String[] paramList = idemRequest.getParamList();
        if (isCanEnter(headersList, servletRequest)) {
            for (String param : paramList) {
                String parameter = servletRequest.getParameter(param);
                if (!StrUtil.isEmpty(parameter)) {
                    CommonUtil.addPrefix(result);
                    result.append(parameter);
                }
            }
        }

        return result.toString();
    }

    private boolean isCanEnter(String[] ObList, HttpServletRequest servletRequest) {
        return !ArrayUtil.isEmpty(ObList) && !ObjectUtil.isEmpty(servletRequest);
    }
}