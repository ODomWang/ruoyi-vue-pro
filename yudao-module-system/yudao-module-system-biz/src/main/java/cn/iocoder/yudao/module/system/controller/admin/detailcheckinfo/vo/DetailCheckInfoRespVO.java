package cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 详情检测信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DetailCheckInfoRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21961")
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

    @Schema(description = "源地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("源地址")
    private String sourceUrl;



}