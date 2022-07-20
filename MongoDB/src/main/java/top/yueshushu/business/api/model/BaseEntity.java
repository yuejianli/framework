package top.yueshushu.business.api.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 基本的实体
 */
@Data
public class BaseEntity {
    /**
     * 最后一次数据投递时间
     */
    private LocalDateTime lastDeliveryTime = LocalDateTime.now();
}
