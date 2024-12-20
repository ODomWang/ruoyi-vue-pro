package cn.iocoder.yudao.module.wenxun.controller.admin.urlchangelog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 网站更新检查-日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UrlChangeLogPageReqVO extends PageParam {

    @Schema(description = "地址名称", example = "李四")
    private String urlName;

    @Schema(description = "主键，同爬虫主键相同", example = "6672")
    private Integer spiderId;

    @Schema(description = "状态（1正常 0停用）", example = "2")
    private Integer status;

    @Schema(description = "首次检测时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "url", example = "https://www.iocoder.cn")
    private String url;

}