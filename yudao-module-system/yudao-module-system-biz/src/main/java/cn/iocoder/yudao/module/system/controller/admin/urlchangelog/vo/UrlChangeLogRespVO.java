package cn.iocoder.yudao.module.system.controller.admin.urlchangelog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 网站更新检查-日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UrlChangeLogRespVO {

    @Schema(description = "主键，同爬虫配置表主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "17172")
    @ExcelProperty("主键，同爬虫配置表主键相同")
    private Integer id;

    @Schema(description = "地址名称", example = "李四")
    @ExcelProperty("地址名称")
    private String urlName;

    @Schema(description = "主键，同爬虫主键相同", requiredMode = Schema.RequiredMode.REQUIRED, example = "6672")
    @ExcelProperty("主键，同爬虫主键相同")
    private Integer spiderId;

    @Schema(description = "状态（1正常 0停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态（1正常 0停用）")
    private Integer status;

    @Schema(description = "首次检测时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("首次检测时间")
    private LocalDateTime createTime;

    @Schema(description = "标题")
    @ExcelProperty("标题")
    private String title;

    @Schema(description = "url", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("url")
    private String url;

}