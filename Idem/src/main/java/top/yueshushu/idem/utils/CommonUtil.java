package top.yueshushu.idem.utils;


/**
 * @auther yjl
 * @date 2022/6/1
 */
public final class CommonUtil {

    public static void addPrefix(StringBuilder builder) {
        if (builder != null && builder.length() > 0) {
            builder.append(":");
        }
    }
}
