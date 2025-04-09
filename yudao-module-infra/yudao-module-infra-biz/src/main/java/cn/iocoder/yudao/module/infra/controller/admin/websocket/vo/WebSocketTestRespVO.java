package cn.iocoder.yudao.module.infra.controller.admin.websocket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - WebSocket测试 Response VO")
@Data
@Accessors(chain = true)
public class WebSocketTestRespVO {

    @Schema(description = "测试 Token", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String token;

} 