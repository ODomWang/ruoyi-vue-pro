package cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 爬虫来源统计 Response VO")
@Data
public class SpiderSourceRespVO {
    
    @Schema(description = "来源名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "新浪新闻")
    private String sourceName;

    @Schema(description = "采集数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    private Long count;

    @Schema(description = "成功率", requiredMode = Schema.RequiredMode.REQUIRED, example = "95.5")
    private BigDecimal successRate;
} 