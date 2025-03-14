package cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 人工研判审核分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AuditLogPageReqVO extends PageParam {

    @Schema(description = "文章校验表id", example = "12180")
    private String spiderId;

    @Schema(description = "审核通过信息")
    private String approvedRecord;

    @Schema(description = "审核驳回信息")
    private String rejectedRecord;

    @Schema(description = "审核状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "操作员名称")
    private String updater;

}