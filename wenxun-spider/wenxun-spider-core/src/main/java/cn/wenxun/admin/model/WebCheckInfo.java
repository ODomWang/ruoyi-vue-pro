package cn.wenxun.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WebCheckInfo {
    @Schema(description = "检测内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "检测内容不能为空")
    public String content;
}
