package top.yueshushu.idem.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author yjl
 * @date 2022/5/19
 */
public class PrintUtil {

    /**
     * (输入输出)参数最大输出长度. -1表示不限制
     */
    private static final int paramMaxPrintLength = 200;

    public static String toJsonString(Object result) {

        if (ObjectUtil.isEmpty(result)) {
            return "";
        }

        String json = JSONUtil.toJsonStr(result);
        if (paramMaxPrintLength <= 0) {
            return json;
        }

        if (json.length() > paramMaxPrintLength) {
            return json.substring(0, paramMaxPrintLength) + "...";
        }

        return json;
    }
}
