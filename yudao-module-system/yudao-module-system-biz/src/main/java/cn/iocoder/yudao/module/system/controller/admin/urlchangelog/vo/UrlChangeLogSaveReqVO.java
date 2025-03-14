package cn.iocoder.yudao.module.system.controller.admin.urlchangelog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 网站更新检查-日志新增/修改 Request VO")
@Data
public class UrlChangeLogSaveReqVO {

    @Schema(description = "主键，同爬虫配置表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "17172")
    private Integer id;

    @Schema(description = "地址名称", example = "李四")
    private String urlName;

    @Schema(description = "主键，同爬虫主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "6672")
    @NotNull(message = "主键，同爬虫主键相同不能为空")
    private Integer spiderId;

    @Schema(description = "状态（1正常 0停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态（1正常 0停用）不能为空")
    private Integer status;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "url", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "url不能为空")
    private String url;

}