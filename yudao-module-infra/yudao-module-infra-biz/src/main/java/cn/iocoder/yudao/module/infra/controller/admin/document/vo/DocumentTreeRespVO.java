package cn.iocoder.yudao.module.infra.controller.admin.document.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 文档树形结构 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DocumentTreeRespVO extends DocumentBaseVO {

    @Schema(description = "文档编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "父文档编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Long parentId;

    @Schema(description = "文档类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "最后更新人", example = "芋道")
    private String updater;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

    @Schema(description = "最后更新时间", required = true)
    private LocalDateTime updateTime;

    @Schema(description = "子文档列表", required = true)
    private List<DocumentTreeRespVO> children;

} 