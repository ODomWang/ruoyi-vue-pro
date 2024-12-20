package cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 文巡-在线检测草稿 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DraftDataRespVO {

    @Schema(description = "草稿id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1329")
    @ExcelProperty("草稿id")
    private Long id;

    @Schema(description = "草稿详情", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("草稿详情")
    private String content;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}