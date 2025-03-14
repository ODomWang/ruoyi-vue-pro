package cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 网页连通记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UrlPingInfoRespVO {

    @Schema(description = "主键，同爬虫配置表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "19052")
    @ExcelProperty("主键，同爬虫配置表主键相同")
    private Integer id;

    @Schema(description = "地址名称", example = "赵六")
    @ExcelProperty("地址名称")
    private String urlName;

    @Schema(description = "总检查次数", example = "15020")
    @ExcelProperty("总检查次数")
    private Integer allCount;

    @Schema(description = "连通成功次数", example = "15854")
    @ExcelProperty("连通成功次数")
    private Integer successCount;

    @Schema(description = "异常检查次数", example = "10333")
    @ExcelProperty("异常检查次数")
    private Integer failCount;

    @Schema(description = "首次检测时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("首次检测时间")
    private LocalDateTime updateTime;

    private String url;


}