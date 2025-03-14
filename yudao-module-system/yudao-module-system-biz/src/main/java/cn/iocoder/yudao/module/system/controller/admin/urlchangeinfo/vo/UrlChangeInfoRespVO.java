package cn.iocoder.yudao.module.system.controller.admin.urlchangeinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 网站更新检查 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UrlChangeInfoRespVO {

    @Schema(description = "主键，同爬虫配置表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "20879")
    @ExcelProperty("主键，同爬虫配置表主键相同")
    private Integer id;

    @Schema(description = "地址名称", example = "王五")
    @ExcelProperty("地址名称")
    private String urlName;

    @Schema(description = "总检查次数", example = "15772")
    @ExcelProperty("总检查次数")
    private Integer allCount;

    @Schema(description = "更新天数", example = "6012")
    @ExcelProperty("更新天数")
    private Integer successCount;

    @Schema(description = "未更新天数", example = "27924")
    @ExcelProperty("未更新天数")
    private Integer failCount;

    @Schema(description = "最后检测时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("最后检测时间")
    private LocalDateTime updateTime;

    @Schema(description = "url", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("url")
    private String url;

    @Schema(description = "最后标题")
    @ExcelProperty("最后标题")
    private String lastTitle;

}