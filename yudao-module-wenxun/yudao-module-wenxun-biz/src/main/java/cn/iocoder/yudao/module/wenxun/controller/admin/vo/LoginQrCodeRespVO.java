package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "登录二维码 Response VO")
@Data
public class LoginQrCodeRespVO {

    @Schema(description = "二维码图片base64", required = true)
    private String qrImgBase64;

    @Schema(description = "二维码内容", required = true)
    private String qrData;

    @Schema(description = "appId", required = true)
    private String appId;

    @Schema(description = "uuid", required = true)
    private String uuid;



} 