package cn.iocoder.yudao.module.wenxun.controller.admin.urlchangeinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;


@Schema(description = "管理后台 - 网站更新检查新增/修改 Request VO")
@Data
public class UrlChangeInfoSaveReqVO {

    @Schema(description = "主键，同爬虫配置表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "20879")
    private Integer id;

    @Schema(description = "地址名称", example = "王五")
    private String urlName;

    @Schema(description = "总检查次数", example = "15772")
    private Integer allCount;

    @Schema(description = "更新天数", example = "6012")
    private Integer successCount;

    @Schema(description = "未更新天数", example = "27924")
    private Integer failCount;

    @Schema(description = "url", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "url不能为空")
    private String url;

    @Schema(description = "最后标题")
    private String lastTitle;

}