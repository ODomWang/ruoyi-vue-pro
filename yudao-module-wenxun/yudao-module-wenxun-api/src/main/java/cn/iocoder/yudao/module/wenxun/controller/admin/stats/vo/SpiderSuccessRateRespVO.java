package cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 爬虫成功率统计 Response VO")
@Data
public class SpiderSuccessRateRespVO {
    
    @Schema(description = "总采集成功率", requiredMode = Schema.RequiredMode.REQUIRED, example = "95.5")
    private Long totalSuccessRate;

    @Schema(description = "今日成功率", requiredMode = Schema.RequiredMode.REQUIRED, example = "98.5")
    private Long todaySuccessRate;

    @Schema(description = "本周成功率", requiredMode = Schema.RequiredMode.REQUIRED, example = "96.5")
    private Long weekSuccessRate;

    @Schema(description = "本月成功率", requiredMode = Schema.RequiredMode.REQUIRED, example = "97.5")
    private Long monthSuccessRate;
} 