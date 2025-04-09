package cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 审核统计请求 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuditStatsReqVO extends StatsBaseReqVO {

    @Schema(description = "部门ID")
    private Long deptId;
    
    @Schema(description = "是否只查看当前用户的审核记录，默认false表示管理员查看全部，普通用户自动设为true")
    private Boolean selfOnly = false;
    
    @Schema(description = "开始时间")
    private LocalDateTime beginTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
} 