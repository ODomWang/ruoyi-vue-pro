package cn.iocoder.yudao.module.infra.enums.document;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档状态枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum DocumentStatusEnum {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    ARCHIVED(2, "已归档");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

} 