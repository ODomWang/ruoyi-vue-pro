package cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 爬虫数量统计 Response VO")
@Data
public class SpiderCountRespVO {
    
    @Schema(description = "总数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long totalCount;

    @Schema(description = "今日数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long todayCount;

    @Schema(description = "本周数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "500")
    private Long weekCount;

    @Schema(description = "本月数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2000")
    private Long monthCount;

    @Schema(description = "环比增长率", requiredMode = Schema.RequiredMode.REQUIRED, example = "10.5")
    private BigDecimal growthRate;
} 