package cn.iocoder.yudao.module.wenxun.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
public class WebCheckInfo {
    @Schema(description = "检测内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotEmpty(message = "检测内容不能为空")
    public String content;
}
