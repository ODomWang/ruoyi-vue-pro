package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 错误分析数据 Response VO")
@Data
public class DashboardErrorAnalysisRespVO {

    @Schema(description = "错误类型分布", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ErrorTypeItem> errorTypes;

    @Schema(description = "常见错误Top10", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<CommonError> commonErrors;

    @Schema(description = "错误示例", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ErrorExample> errorExamples;

    @Schema(description = "错误类型项")
    @Data
    public static class ErrorTypeItem {

        @Schema(description = "错误类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "错别字")
        private String type;

        @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "256")
        private Integer count;
    }

    @Schema(description = "常见错误")
    @Data
    public static class CommonError {

        @Schema(description = "错误词", requiredMode = Schema.RequiredMode.REQUIRED, example = "中国人民共何国")
        private String wrongWord;

        @Schema(description = "正确词", requiredMode = Schema.RequiredMode.REQUIRED, example = "中国人民共和国")
        private String rightWord;

        @Schema(description = "出现次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "42")
        private Integer count;
    }

    @Schema(description = "错误示例")
    @Data
    public static class ErrorExample {

        @Schema(description = "错误ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long id;

        @Schema(description = "错误词", requiredMode = Schema.RequiredMode.REQUIRED, example = "中国人民共何国")
        private String wrongWord;

        @Schema(description = "正确词", requiredMode = Schema.RequiredMode.REQUIRED, example = "中国人民共和国")
        private String rightWord;

        @Schema(description = "上下文", requiredMode = Schema.RequiredMode.REQUIRED, example = "...在中国人民共何国成立70周年之际...")
        private String context;

        @Schema(description = "文档标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "国庆70周年庆典报道")
        private String documentTitle;
    }
} 