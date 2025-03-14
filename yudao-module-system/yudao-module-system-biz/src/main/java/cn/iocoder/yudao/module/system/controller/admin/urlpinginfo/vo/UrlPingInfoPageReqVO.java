package cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 网页连通记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UrlPingInfoPageReqVO extends PageParam {

    @Schema(description = "地址名称", example = "赵六")
    private String urlName;

    @Schema(description = "总检查次数", example = "15020")
    private Integer allCount;

    @Schema(description = "连通成功次数", example = "15854")
    private Integer successCount;

    @Schema(description = "异常检查次数", example = "10333")
    private Integer failCount;

    @Schema(description = "首次检测时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}