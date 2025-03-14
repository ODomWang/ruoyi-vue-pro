package cn.iocoder.yudao.module.system.controller.admin.urlchangeinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 网站更新检查分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UrlChangeInfoPageReqVO extends PageParam {

    @Schema(description = "地址名称", example = "王五")
    private String urlName;

    @Schema(description = "总检查次数", example = "15772")
    private Integer allCount;

    @Schema(description = "更新天数", example = "6012")
    private Integer successCount;

    @Schema(description = "未更新天数", example = "27924")
    private Integer failCount;

    @Schema(description = "首次检测时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "url", example = "https://www.iocoder.cn")
    private String url;

    @Schema(description = "最后标题")
    private String lastTitle;

}