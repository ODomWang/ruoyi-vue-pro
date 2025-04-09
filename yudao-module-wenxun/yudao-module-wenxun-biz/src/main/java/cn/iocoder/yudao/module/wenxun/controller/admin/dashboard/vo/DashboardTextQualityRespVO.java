package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 文本质量分析 Response VO")
@Data
public class DashboardTextQualityRespVO {

    @Schema(description = "质量评分趋势", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<TrendPoint> qualityScoreTrend;

    @Schema(description = "质量分布", requiredMode = Schema.RequiredMode.REQUIRED)
    private QualityDistribution qualityDistribution;

    @Schema(description = "优质文档示例", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ExemplaryDocument> exemplaryDocuments;

    @Schema(description = "趋势点")
    @Data
    public static class TrendPoint {

        @Schema(description = "日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01")
        private String date;

        @Schema(description = "数值", requiredMode = Schema.RequiredMode.REQUIRED, example = "85.6")
        private Double value;
    }

    @Schema(description = "质量分布")
    @Data
    public static class QualityDistribution {

        @Schema(description = "优秀", requiredMode = Schema.RequiredMode.REQUIRED, example = "156")
        private Integer excellent;

        @Schema(description = "良好", requiredMode = Schema.RequiredMode.REQUIRED, example = "312")
        private Integer good;

        @Schema(description = "一般", requiredMode = Schema.RequiredMode.REQUIRED, example = "125")
        private Integer average;

        @Schema(description = "较差", requiredMode = Schema.RequiredMode.REQUIRED, example = "43")
        private Integer poor;
    }

    @Schema(description = "优质文档示例")
    @Data
    public static class ExemplaryDocument {

        @Schema(description = "文档ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long id;

        @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "深入学习贯彻习近平新时代中国特色社会主义思想")
        private String title;

        @Schema(description = "来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "人民日报")
        private String source;

        @Schema(description = "日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-05-15")
        private String date;

        @Schema(description = "质量评分", requiredMode = Schema.RequiredMode.REQUIRED, example = "95")
        private Integer qualityScore;
    }
} 