package cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 审核统计 Response VO")
@Data
public class AuditStatsRespVO {

    @Schema(description = "总审核数")
    private Integer totalCount;

    @Schema(description = "通过数量")
    private Integer approvedCount;

    @Schema(description = "驳回数量")
    private Integer rejectedCount;

    @Schema(description = "待审核数量")
    private Integer pendingCount;

    @Schema(description = "审核通过率")
    private Double approvalRate;

    @Schema(description = "各状态占比")
    private List<Map<String, Object>> statusDistribution;

    @Schema(description = "部门审核分布")
    private List<Map<String, Object>> deptDistribution;

    @Schema(description = "最近七天审核趋势")
    private List<Map<String, Object>> recentTrend;
} 