package top.yueshushu.idem.exception;

import lombok.Getter;
import lombok.Setter;
import top.yueshushu.idem.core.IdemResponse;

/**
 * @author yjl
 * @date 2022/5/24
 */
@Setter
@Getter
public class IdemCacheWrapper extends Exception {
    private IdemResponse idemResponse;

    public IdemCacheWrapper(IdemResponse idemResponse) {
        this.idemResponse = idemResponse;
    }


}