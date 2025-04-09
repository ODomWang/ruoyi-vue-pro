package cn.iocoder.yudao.module.infra.enums.document;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档分享状态枚举
 */
@Getter
@AllArgsConstructor
public enum DocumentShareStatusEnum {

    NORMAL(0, "正常"),
    EXPIRED(1, "已过期"),
    DISABLED(2, "已禁用");

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 名字
     */
    private final String name;

} 