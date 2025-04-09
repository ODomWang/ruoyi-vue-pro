package cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 爬虫趋势统计 Response VO")
@Data
public class SpiderTrendRespVO {
    
    @Schema(description = "时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01-01")
    private String time;

    @Schema(description = "采集数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long count;

    @Schema(description = "成功数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "95")
    private Long successCount;

    @Schema(description = "失败数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long failCount;
} 