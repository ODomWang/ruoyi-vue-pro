package cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 文巡-在线检测草稿新增/修改 Request VO")
@Data
public class DraftDataSaveReqVO {

    @Schema(description = "草稿id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1329")
    private Long id;

    @Schema(description = "草稿详情", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "草稿详情不能为空")
    private String content;

    @Schema(description = "备注", example = "-")
    private String remark;

}