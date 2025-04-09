package cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 统计基础请求 VO")
@Data
public class StatsBaseReqVO {
    
    @Schema(description = "统计维度列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"time\", \"dept\"]")
    private List<String> dimensions;

    @Schema(description = "时间粒度", requiredMode = Schema.RequiredMode.REQUIRED, example = "day")
    private String timeGranularity;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "部门编号", example = "1024")
    private Long deptId;
} 