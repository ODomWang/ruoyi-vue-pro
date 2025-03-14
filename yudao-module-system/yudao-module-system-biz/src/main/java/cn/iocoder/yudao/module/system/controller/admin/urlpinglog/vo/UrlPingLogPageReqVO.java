package cn.iocoder.yudao.module.system.controller.admin.urlpinglog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 网页连通记录-日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UrlPingLogPageReqVO extends PageParam {

    @Schema(description = "地址名称", example = "芋艿")
    private String urlName;

    @Schema(description = "主键，同连通表主键相同", example = "21567")
    private Integer pingId;

    @Schema(description = "状态（1正常 0停用）", example = "1")
    private Integer status;

    @Schema(description = "首次检测时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}