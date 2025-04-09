package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "文巡登录 Response VO")
@Data
public class WenxunLoginRespVO {

    @Schema(description = "应用ID", required = true, example = "wx123456")
    private String appId;

    @Schema(description = "二维码图片base64", required = true)
    private String qrImgBase64;

    @Schema(description = "二维码内容", required = true)
    private String qrData;

    @Schema(description = "登录状态", required = true, example = "0")
    private Integer status;

    @Schema(description = "用户信息")
    private WenxunUserRespVO user;

    @Schema(description = "uuid")
    private String uuid;

} 