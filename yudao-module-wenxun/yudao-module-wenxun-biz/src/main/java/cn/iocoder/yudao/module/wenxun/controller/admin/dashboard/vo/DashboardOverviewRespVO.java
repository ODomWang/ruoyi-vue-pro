package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 数字大屏概览 Response VO")
@Data
public class DashboardOverviewRespVO {

    @Schema(description = "总文档数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer totalDocuments;

    @Schema(description = "有错误的文档数", requiredMode = Schema.RequiredMode.REQUIRED, example = "128")
    private Integer documentsWithErrors;

    @Schema(description = "总错误数", requiredMode = Schema.RequiredMode.REQUIRED, example = "256")
    private Integer totalErrors;

    @Schema(description = "监测网站数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer monitoredSites;

    @Schema(description = "错误率(%)", requiredMode = Schema.RequiredMode.REQUIRED, example = "12.5")
    private Double errorRate;

    @Schema(description = "网站状态列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SiteStatus> siteStatus;

    @Schema(description = "文档趋势", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<TrendPoint> documentTrend;

    @Schema(description = "错误率趋势", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<TrendPoint> errorRateTrend;

    @Schema(description = "文档分布", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<DistributionItem> documentDistribution;

    @Schema(description = "网站状态信息")
    @Data
    public static class SiteStatus {

        @Schema(description = "网站ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Integer id;

        @Schema(description = "网站名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "政府门户网站")
        private String name;

        @Schema(description = "网站URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.example.gov.cn")
        private String url;

        @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "正常")
        private String status;

        @Schema(description = "最后检测时间", requiredMode = Schema.RequiredMode.REQUIRED)
        private String lastPingTime;
    }

    @Schema(description = "趋势点")
    @Data
    public static class TrendPoint {

        @Schema(description = "日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01")
        private String date;

        @Schema(description = "数值", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        private Number value;
    }

    @Schema(description = "分布项")
    @Data
    public static class DistributionItem {

        @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "政府部门")
        private String name;

        @Schema(description = "数值", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        private Integer value;
    }
} 