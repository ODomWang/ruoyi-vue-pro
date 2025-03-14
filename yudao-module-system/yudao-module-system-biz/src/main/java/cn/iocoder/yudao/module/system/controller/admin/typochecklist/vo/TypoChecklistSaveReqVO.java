package cn.iocoder.yudao.module.system.controller.admin.typochecklist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 错词检查新增/修改 Request VO")
@Data
public class TypoChecklistSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1633")
    private Long id;

    @Schema(description = "异常词汇", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "异常词汇不能为空")
    private String typo;

    @Schema(description = "校政词汇", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "校政词汇不能为空")
    private String correction;

    @Schema(description = "数据状态。0未修复，1已修复，2不存在，3.无需处理", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "数据状态。0未修复，1已修复，2不存在，3.无需处理不能为空")
    private Integer status;

    @Schema(description = "文章地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "文章地址不能为空")
    private String spiderUrl;

    @Schema(description = "错词等级", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotEmpty(message = "错词等级不能为空")
    private String colorType;

}