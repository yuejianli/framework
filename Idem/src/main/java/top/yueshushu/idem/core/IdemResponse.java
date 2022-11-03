package top.yueshushu.idem.core;

import lombok.Data;
import top.yueshushu.idem.utils.PrintUtil;

import java.io.Serializable;

/**
 * @author yjl
 * @date 2022/5/24
 */
@Data
public class IdemResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object processResult;

    @Override
    public String toString() {
        return "IdemResponse{" +
                "processResult=" + PrintUtil.toJsonString(processResult) +
                '}';
    }
}
