package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 登录 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppAuthLoginRespVO {

    @Schema(description = "返回状态码", required = true, example = "200")
    private Integer ret;

    @Schema(description = "返回信息", required = true, example = "操作成功")
    private String msg;

    @Schema(description = "返回数据 - token", required = true, example = "abc123")
    private String data;

} 