package cn.iocoder.yudao.module.wenxun.controller.admin.customerauditlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 客户审核 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CustomerAuditLogRespVO {

    @Schema(description = "住建", requiredMode = Schema.RequiredMode.REQUIRED, example = "2946")
    @ExcelProperty("住建")
    private Integer id;

    @Schema(description = "文章校验表id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23291")
    @ExcelProperty("文章校验表id")
    private String spiderId;

    @Schema(description = "审核通过信息")
    @ExcelProperty("审核通过信息")
    private String approvedRecord;

    @Schema(description = "审核驳回信息")
    @ExcelProperty("审核驳回信息")
    private String rejectedRecord;

    @Schema(description = "审核状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("审核状态")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "操作员名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("操作员名称")
    private String auditor;

}