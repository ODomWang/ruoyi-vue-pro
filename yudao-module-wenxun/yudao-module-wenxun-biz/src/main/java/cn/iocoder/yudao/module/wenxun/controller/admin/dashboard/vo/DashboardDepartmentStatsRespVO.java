package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 部门工作统计 Response VO")
@Data
public class DashboardDepartmentStatsRespVO {

    @Schema(description = "部门统计列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<DepartmentStat> departments;

    @Schema(description = "部门统计数据")
    @Data
    public static class DepartmentStat {

        @Schema(description = "部门ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long id;

        @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "新闻编辑部")
        private String name;

        @Schema(description = "文档数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "256")
        private Integer documentCount;

        @Schema(description = "错误数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "32")
        private Integer errorCount;

        @Schema(description = "错误率(%)", requiredMode = Schema.RequiredMode.REQUIRED, example = "12.5")
        private Double errorRate;

        @Schema(description = "平均处理时间(分钟)", requiredMode = Schema.RequiredMode.REQUIRED, example = "45.3")
        private Double avgProcessingTime;
    }
} 