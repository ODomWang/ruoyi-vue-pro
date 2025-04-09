package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "管理后台 - 联系人更新 Request VO")
@Data
public class ContactUpdateReqVO {

    @Schema(description = "微信ID", required = true, example = "wxid_123456")
    @NotEmpty(message = "微信ID不能为空")
    private String wxId;

    @Schema(description = "备注", example = "同事")
    private String remark;
    
    @Schema(description = "标签列表", example = "['朋友', '同事']")
    private String[] tags;
} 