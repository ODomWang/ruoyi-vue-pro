package cn.wenxun.admin.model.spider;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@TableName("wenxun_spider_source_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WenxunSpiderSourceConfigDO {

    @TableId
    private int id;
    private String spiderUrl;
    private String spiderName;
    private String spiderModel;
    private long pingStatus;
    private long status;
     @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;
    private long deptId;
    private String creator;
    private String updater;
    private String remark;
    private long spiderPageNum;


}
