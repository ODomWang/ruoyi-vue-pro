package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 网站监测数据 Response VO")
@Data
public class DashboardSitesMonitoringRespVO {

    @Schema(description = "网站列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SiteDetail> sites;

    @Schema(description = "可用性趋势", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<TrendPoint> availabilityTrend;

    @Schema(description = "网站详情")
    @Data
    public static class SiteDetail {

        @Schema(description = "网站ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Integer id;

        @Schema(description = "网站名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "政府门户网站")
        private String name;

        @Schema(description = "网站URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.example.gov.cn")
        private String url;

        @Schema(description = "文档数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Integer documentCount;

        @Schema(description = "成功率", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.5")
        private Double successRate;

        @Schema(description = "平均响应时间(ms)", requiredMode = Schema.RequiredMode.REQUIRED, example = "200")
        private Double avgResponseTime;
    }

    @Schema(description = "趋势点")
    @Data
    public static class TrendPoint {

        @Schema(description = "日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01")
        private String date;

        @Schema(description = "数值", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.8")
        private Double value;
    }
} 