package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "管理后台 - 时间选项数据 Response VO")
@Data
public class DashboardTimeOptionsRespVO {

    @Schema(description = "时间选项列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Option> options;

    @Schema(description = "自定义时间范围", requiredMode = Schema.RequiredMode.REQUIRED)
    private CustomRange customRange;

    @Schema(description = "时间选项")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {

        @Schema(description = "选项值", requiredMode = Schema.RequiredMode.REQUIRED, example = "last7days")
        private String value;

        @Schema(description = "选项标签", requiredMode = Schema.RequiredMode.REQUIRED, example = "最近7天")
        private String label;
    }

    @Schema(description = "自定义时间范围")
    @Data
    public static class CustomRange {

        @Schema(description = "最小日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01")
        private String min;

        @Schema(description = "最大日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-12-31")
        private String max;
    }
} 