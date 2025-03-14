package cn.iocoder.yudao.module.system.controller.admin.urlpinglog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 网页连通记录-日志新增/修改 Request VO")
@Data
public class UrlPingLogSaveReqVO {

    @Schema(description = "主键，同爬虫配置表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "19551")
    private Integer id;

    @Schema(description = "地址名称", example = "芋艿")
    private String urlName;

    @Schema(description = "主键，同连通表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "21567")
    @NotNull(message = "主键，同连通表主键相同不能为空")
    private Integer pingId;

    @Schema(description = "状态（1正常 0停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态（1正常 0停用）不能为空")
    private Integer status;

    @Schema(description = "网络状态码", example = "200")
     private String pingCode;

    private String url;

    private String updater;

}