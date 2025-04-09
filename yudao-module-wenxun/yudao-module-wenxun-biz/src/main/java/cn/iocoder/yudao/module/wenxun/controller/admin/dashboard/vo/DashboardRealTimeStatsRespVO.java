package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 实时统计数据 Response VO")
@Data
public class DashboardRealTimeStatsRespVO {

    @Schema(description = "今日文档数", requiredMode = Schema.RequiredMode.REQUIRED, example = "112")
    private Integer todayDocuments;

    @Schema(description = "今日错误数", requiredMode = Schema.RequiredMode.REQUIRED, example = "18")
    private Integer todayErrors;

    @Schema(description = "最新文档", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<LatestDocument> latestDocuments;

    @Schema(description = "最新错误", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<LatestError> latestErrors;

    @Schema(description = "最新文档")
    @Data
    public static class LatestDocument {

        @Schema(description = "文档ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long id;

        @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "关于加强生态环境保护的通知")
        private String title;

        @Schema(description = "来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "环保部网站")
        private String source;

        @Schema(description = "时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-15 15:30:00")
        private String timestamp;
    }

    @Schema(description = "最新错误")
    @Data
    public static class LatestError {

        @Schema(description = "错误ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
        private Long id;

        @Schema(description = "文档ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long documentId;

        @Schema(description = "文档标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "关于加强生态环境保护的通知")
        private String documentTitle;

        @Schema(description = "错误词", requiredMode = Schema.RequiredMode.REQUIRED, example = "环保报")
        private String wrongWord;

        @Schema(description = "正确词", requiredMode = Schema.RequiredMode.REQUIRED, example = "环保部")
        private String rightWord;

        @Schema(description = "时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-15 15:35:21")
        private String timestamp;
    }
} 