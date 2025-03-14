package cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 详情检测信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DetailCheckInfoWithDictDataRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21961")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "检查源，1 敏感词，2，错词，3，接口", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("检查源，1 敏感词，2，错词，3，接口")
    private Integer checkSource;

    @Schema(description = "错词详情")
    @ExcelProperty("错词详情")
    private String checkDetail;

    @Schema(description = "修正词汇", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("修正词汇")
    private String targetDetail;

    @Schema(description = "数据状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("数据状态")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "源地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("源地址")
    private String sourceUrl;

    @Schema(description = "源地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("源地址")
    private String webIcon;

    @Schema(description = "敏感词字典", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("敏感词字典")
    private PageResult<WenXunDictDataDO> dictDataDOS;


    @Schema(description = "采集任务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("采集任务名称")
    private String spiderName;





}