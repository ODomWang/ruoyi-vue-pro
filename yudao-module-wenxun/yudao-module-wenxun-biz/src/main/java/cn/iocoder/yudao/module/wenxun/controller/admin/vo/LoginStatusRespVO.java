package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 登录状态 Response VO")
@Data
public class LoginStatusRespVO {

    @Schema(description = "是否登录成功", required = true, example = "true")
    private Boolean success;

    @Schema(description = "微信ID", example = "wxid_123456")
    private String wxId;

    @Schema(description = "错误信息", example = "二维码已过期")
    private String errorMsg;

} 