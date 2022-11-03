package top.yueshushu.idem.exception;

/**
 * @author yjl
 * @date 2022/6/1
 */
public final class IdemNotFindKeyException extends RuntimeException {
    private IdemNotFindKeyException() {
    }

    public IdemNotFindKeyException(String message) {
        super(message);
    }
}
