package top.yueshushu.idem.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yjl
 * @date 2022/5/19
 */
@Setter
@Getter
public final class IdemEntryException extends RuntimeException {
    private IdemEntryException() {
    }

    public IdemEntryException(String message) {
        super(message);
    }
}
