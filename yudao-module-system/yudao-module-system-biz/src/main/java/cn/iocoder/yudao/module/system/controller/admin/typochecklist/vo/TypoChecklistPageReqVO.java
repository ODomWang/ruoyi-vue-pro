package cn.iocoder.yudao.module.system.controller.admin.typochecklist.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 错词检查分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TypoChecklistPageReqVO extends PageParam {

    @Schema(description = "异常词汇")
    private String typo;

    @Schema(description = "校政词汇")
    private String correction;

    @Schema(description = "数据状态。0未修复，1已修复，2不存在，3.无需处理", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "文章地址", example = "https://www.iocoder.cn")
    private String spiderUrl;

    @Schema(description = "错词等级", example = "2")
    private String colorType;

}