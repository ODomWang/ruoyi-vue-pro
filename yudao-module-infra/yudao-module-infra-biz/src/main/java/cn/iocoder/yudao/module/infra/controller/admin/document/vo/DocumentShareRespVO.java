package cn.iocoder.yudao.module.infra.controller.admin.document.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 文档分享 Response VO")
@Data
public class DocumentShareRespVO {

    @Schema(description = "分享编号", required = true, example = "1024")
    private Long id;

    @Schema(description = "分享链接的标识", required = true, example = "a1b2c3d4")
    private String shareId;

    @Schema(description = "文档编号", required = true, example = "2048")
    private Long documentId;

    @Schema(description = "创建人的用户编号", required = true, example = "1")
    private Long userId;

    @Schema(description = "是否需要密码访问", required = true, example = "true")
    private Boolean passwordProtected;

    @Schema(description = "过期时间", example = "2024-12-31 23:59:59")
    private String expireTime;

    @Schema(description = "分享状态", required = true, example = "0")
    private Integer status;

    @Schema(description = "创建时间", required = true, example = "2022-07-01 12:00:00")
    private LocalDateTime createTime;

    @Schema(description = "备注", example = "这是一份重要文档")
    private String remark;
} 