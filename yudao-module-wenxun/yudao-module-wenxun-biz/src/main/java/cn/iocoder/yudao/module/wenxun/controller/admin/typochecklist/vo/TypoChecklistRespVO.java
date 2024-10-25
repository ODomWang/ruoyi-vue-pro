package cn.iocoder.yudao.module.wenxun.controller.admin.typochecklist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 错词检查 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TypoChecklistRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1633")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "异常词汇", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("异常词汇")
    private String typo;

    @Schema(description = "校政词汇", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("校政词汇")
    private String correction;

    @Schema(description = "数据状态。0未修复，1已修复，2不存在，3.无需处理", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("数据状态。0未修复，1已修复，2不存在，3.无需处理")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "文章地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("文章地址")
    private String spiderUrl;

    @Schema(description = "错词等级", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("错词等级")
    private String colorType;

}