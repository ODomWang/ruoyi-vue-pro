package cn.iocoder.yudao.module.wenxun.controller.admin.urlpinglog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 网页连通记录-日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UrlPingLogRespVO {

    @Schema(description = "主键，同爬虫配置表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "19551")
    @ExcelProperty("主键，同爬虫配置表主键相同")
    private Integer id;

    @Schema(description = "地址名称", example = "芋艿")
    @ExcelProperty("地址名称")
    private String urlName;

    @Schema(description = "主键，同连通表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "21567")
    @ExcelProperty("主键，同连通表主键相同")
    private Integer pingId;

    @Schema(description = "状态（1正常 0停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态（1正常 0停用）")
    private Integer status;

    @Schema(description = "首次检测时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("首次检测时间")
    private LocalDateTime updateTime;

    private String url;

    private String pingCode;

    private String updater;

}