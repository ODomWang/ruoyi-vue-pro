package cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 详情检测信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DetailCheckInfoPageReqVO extends PageParam {

    @Schema(description = "检查源，1 敏感词，2，错词，3，接口")
    private Integer checkSource;

    @Schema(description = "错词详情")
    private String checkDetail;

    @Schema(description = "修正词汇")
    private String targetDetail;

    @Schema(description = "数据状态", example = "1")
    private Integer status;

    @Schema(description = "数据源配置id", example = "1")
    private long spiderConfigId;


    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}