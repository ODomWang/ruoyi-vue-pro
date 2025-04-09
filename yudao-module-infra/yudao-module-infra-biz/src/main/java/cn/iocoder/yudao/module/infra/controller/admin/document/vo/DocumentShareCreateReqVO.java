package cn.iocoder.yudao.module.infra.controller.admin.document.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 文档分享创建 Request VO")
@Data
public class DocumentShareCreateReqVO {

    @Schema(description = "文档编号", required = true, example = "1024")
    @NotNull(message = "文档编号不能为空")
    private Long documentId;

    @Schema(description = "分享密码，为空则不需要密码", example = "123456")
    private String password;

    @Schema(description = "过期时间，为空则永久有效", example = "2024-12-31 23:59:59")
    private String expireTime;

    @Schema(description = "备注", example = "这是一份重要文档")
    private String remark;
} 