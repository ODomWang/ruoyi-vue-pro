package cn.iocoder.yudao.module.wenxun.controller.admin.urlpinginfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 网页连通记录新增/修改 Request VO")
@Data
public class UrlPingInfoSaveReqVO {

    @Schema(description = "主键，同爬虫配置表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "19052")
    private Integer id;

    @Schema(description = "地址名称", example = "赵六")
    private String urlName;

    @Schema(description = "总检查次数", example = "15020")
    private Integer allCount;

    @Schema(description = "连通成功次数", example = "15854")
    private Integer successCount;

    @Schema(description = "异常检查次数", example = "10333")
    private Integer failCount;

}