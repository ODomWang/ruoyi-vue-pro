package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "用户 APP - 微信登录 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppAuthWxLoginRespVO {

    @Schema(description = "用户编号", required = true, example = "1024")
    private Long userId;

    @Schema(description = "访问令牌", required = true, example = "token")
    private String accessToken;

    @Schema(description = "刷新令牌", required = true, example = "refresh_token")
    private String refreshToken;

    @Schema(description = "过期时间", required = true, example = "2023-01-01 00:00:00")
    private Long expiresTime;

} 