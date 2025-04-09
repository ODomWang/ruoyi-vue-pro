package cn.iocoder.yudao.module.infra.controller.admin.document.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class DocumentBaseVO {

    @Schema(description = "文档标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "项目文档")
    @NotNull(message = "文档标题不能为空")
    @Size(max = 200, message = "文档标题不能超过 200 个字符")
    private String title;

    @Schema(description = "文档内容", example = "这是一篇项目文档")
    private String content;

    @Schema(description = "文档状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "文档状态不能为空")
    private Integer status;

    @Schema(description = "版本号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer version;

    @Schema(description = "备注", example = "重要文档")
    private String remark;

} 