package cn.iocoder.yudao.module.infra.controller.admin.websocket;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.websocket.config.WebSocketProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 诊断 Controller
 */
@Tag(name = "管理后台 - WebSocket 诊断")
@RestController
@RequestMapping("/infra/websocket-diagnostic")
@RequiredArgsConstructor
@Slf4j
public class WebSocketDiagnosticController {

    private final WebSocketProperties webSocketProperties;

    @GetMapping("/check")
    @Operation(summary = "检查 WebSocket 配置")
    public CommonResult<Map<String, Object>> checkConfig() {
        Map<String, Object> result = new HashMap<>();
        // 获取WebSocket配置
        result.put("websocketPath", webSocketProperties.getPath());
        result.put("websocketEnable", true);
        result.put("websocketSenderType", webSocketProperties.getSenderType());
        
        log.info("[checkConfig][WebSocket诊断] 配置路径={}, 是否启用={}, 发送器类型={}", 
                webSocketProperties.getPath(), true, webSocketProperties.getSenderType());
        
        // 返回详细的服务状态信息
        result.put("serverInfo", "用于WebSocket连接的路径应该是ws://服务器:端口" + webSocketProperties.getPath());
        result.put("javaVersion", System.getProperty("java.version"));
        result.put("osName", System.getProperty("os.name"));
        
        return CommonResult.success(result);
    }
} 