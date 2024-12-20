package cn.iocoder.yudao.module.wenxun.controller.admin.customerauditlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 客户审核新增/修改 Request VO")
@Data
public class CustomerAuditLogSaveReqVO {

    @Schema(description = "住建", requiredMode = Schema.RequiredMode.REQUIRED, example = "2946")
    private Integer id;

    @Schema(description = "文章校验表id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23291")
    @NotEmpty(message = "文章校验表id不能为空")
    private String spiderId;

    @Schema(description = "审核通过信息")
    private String approvedRecord;

    @Schema(description = "审核驳回信息")
    private String rejectedRecord;

    @Schema(description = "审核状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "审核状态不能为空")
    private Integer status;

    @Schema(description = "操作员名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "操作员名称不能为空")
    private String auditor;

}