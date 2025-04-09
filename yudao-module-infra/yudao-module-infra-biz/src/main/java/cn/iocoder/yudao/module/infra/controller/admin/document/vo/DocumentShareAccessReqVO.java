package cn.iocoder.yudao.module.infra.controller.admin.document.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 文档分享访问 Request VO")
@Data
public class DocumentShareAccessReqVO {

    @Schema(description = "分享链接的标识", required = true, example = "a1b2c3d4")
    @NotBlank(message = "分享标识不能为空")
    private String shareId;

    @Schema(description = "访问密码", example = "123456")
    private String password;
} 