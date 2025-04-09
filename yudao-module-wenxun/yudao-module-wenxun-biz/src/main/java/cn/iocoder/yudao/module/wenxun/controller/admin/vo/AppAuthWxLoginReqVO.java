package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Schema(description = "用户 APP - 微信登录 Request VO")
@Data
public class AppAuthWxLoginReqVO {

    @Schema(description = "微信小程序的 code", required = true, example = "aaa")
    @NotEmpty(message = "code 不能为空")
    private String code;

} 