package cn.iocoder.yudao.module.infra.enums.document;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档类型枚举
 */
@Getter
@AllArgsConstructor
public enum DocumentTypeEnum {

    FOLDER(1, "文件夹"),
    FILE(2, "文件");

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名字
     */
    private final String name;

} 