package cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 详情检测信息表-用户审核 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DetailCheckAuditInfoRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "13031")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "检查源，1 敏感词，2，错词，3，接口", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("检查源，1 敏感词，2，错词，3，接口")
    private Integer checkSource;

    @Schema(description = "错词详情")
    @ExcelProperty("错词详情")
    private String checkDetail;

    @Schema(description = "修正词汇", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("修正词汇")
    private String targetDetail;

    @Schema(description = "数据状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("数据状态")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "源网站地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("源网站地址")
    private String sourceUrl;

    @Schema(description = "采集配置id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10755")
    @ExcelProperty("采集配置id")
    private Integer spiderConfigId;

    @Schema(description = "网站图标")
    @ExcelProperty("网站图标")
    private String webIcon;

    @Schema(description = "网站图标")
    @ExcelProperty("网站图标")
    private String titleDesc;

}