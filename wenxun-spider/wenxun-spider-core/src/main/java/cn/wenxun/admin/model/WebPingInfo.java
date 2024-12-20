package cn.wenxun.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WebPingInfo {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "检测内容不能为空")
    public int id;

}
