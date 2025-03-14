package cn.iocoder.yudao.module.system.controller.admin.detailcheckauditinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 详情检测信息表-用户审核分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DetailCheckAuditInfoPageReqVO extends PageParam {

    @Schema(description = "检查源，1 敏感词，2，错词，3，接口")
    private Integer checkSource;

    @Schema(description = "错词详情")
    private String checkDetail;

    @Schema(description = "修正词汇")
    private String targetDetail;

    @Schema(description = "数据状态", example = "1")
    private Integer status;


    @Schema(description = "创建时间")
    private String createTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "创建时间")
    private LocalDateTime[] timeRange;

    @Schema(description = "源网站地址", example = "https://www.iocoder.cn")
    private String sourceUrl;

    @Schema(description = "采集配置id", example = "10755")
    private Integer spiderConfigId;

    @Schema(description = "网站图标")
    private String webIcon;

}