package cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - 爬虫统计响应 VO")
@Data
public class SpiderStatsRespVO {
    
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

    @Schema(description = "部门维度统计")
    private List<DeptStatVO> deptStats;

    @Schema(description = "部门统计数据")
    @Data
    public static class DeptStatVO {
        @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long deptId;

        @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "技术部")
        private String deptName;

        @Schema(description = "数据量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        private Long count;

        @Schema(description = "占比", requiredMode = Schema.RequiredMode.REQUIRED, example = "10.5")
        private BigDecimal percentage;
    }
} 