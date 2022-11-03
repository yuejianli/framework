package top.yueshushu.idem.exception;

/**
 * @author yjl
 * @date 2022/5/24
 */
public class IdemDedupException extends Exception {

    public IdemDedupException(String idemKey) {
        super("IdemDedupException, idemKey[" + idemKey + "] already exists!!!");
    }
}