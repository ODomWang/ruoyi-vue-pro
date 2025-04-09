package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "文巡用户信息 Response VO")
@Data
public class WenxunUserRespVO {

    @Schema(description = "用户编号", required = true, example = "1024")
    private Long id;

    @Schema(description = "应用ID", required = true, example = "wx123456")
    private String appId;

    @Schema(description = "登录状态", required = true, example = "1")
    private Integer status;

    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Schema(description = "用户头像", example = "https://example.com/avatar.jpg")
    private String avatar;

} 