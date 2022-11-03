package top.yueshushu.idem.core.handle;

/**
 * @author yjl
 * @date 2022/5/19
 */
public interface Parser {
    String doParse(Object request) throws Throwable;
}
