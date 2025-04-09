package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 筛选选项数据 Response VO")
@Data
public class DashboardFilterOptionsRespVO {

    @Schema(description = "部门列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Option> departments;

    @Schema(description = "网站列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Option> sites;

    @Schema(description = "错误类型列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Option> errorTypes;

    @Schema(description = "选项")
    @Data
    public static class Option {

        @Schema(description = "选项值", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private String value;

        @Schema(description = "选项标签", requiredMode = Schema.RequiredMode.REQUIRED, example = "新闻编辑部")
        private String label;
    }
} 