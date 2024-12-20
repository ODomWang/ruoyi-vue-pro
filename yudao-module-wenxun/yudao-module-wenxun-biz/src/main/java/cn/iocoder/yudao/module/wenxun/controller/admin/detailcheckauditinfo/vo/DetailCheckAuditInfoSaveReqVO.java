package cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 详情检测信息表-用户审核新增/修改 Request VO")
@Data
public class DetailCheckAuditInfoSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "13031")
    private Long id;

    @Schema(description = "检查源，1 敏感词，2，错词，3，接口", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "检查源，1 敏感词，2，错词，3，接口不能为空")
    private Integer checkSource;

    @Schema(description = "错词详情")
    private String checkDetail;

    @Schema(description = "修正词汇", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "修正词汇不能为空")
    private String targetDetail;

    @Schema(description = "数据状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "数据状态不能为空")
    private Integer status;

    @Schema(description = "源网站地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "源网站地址不能为空")
    private String sourceUrl;

    @Schema(description = "采集配置id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10755")
    @NotNull(message = "采集配置id不能为空")
    private Integer spiderConfigId;

    @Schema(description = "网站图标")
    private String webIcon;

    @Schema(description = "文章描述")
    private String titleDesc;

}