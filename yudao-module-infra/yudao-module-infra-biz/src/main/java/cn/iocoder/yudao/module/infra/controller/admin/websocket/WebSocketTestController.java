package cn.iocoder.yudao.module.infra.controller.admin.websocket;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.websocket.config.WebSocketProperties;
import cn.iocoder.yudao.module.infra.controller.admin.websocket.vo.WebSocketTestRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 测试 Controller
 */
@Tag(name = "管理后台 - WebSocket 测试")
@RestController
@RequestMapping("/infra/websocket-test")
@Validated
@RequiredArgsConstructor
@Slf4j
public class WebSocketTestController {

    private final WebSocketProperties webSocketProperties;

    @GetMapping("/get-token")
    @Operation(summary = "获取 WebSocket 测试 Token")
    public CommonResult<WebSocketTestRespVO> getToken(HttpServletRequest request) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        String token;
        // 情况一，未登录
        if (userId == null) {
            token = "demo";
        // 情况二，已登录
        } else {
            token = SecurityFrameworkUtils.obtainAuthorization(request, 
                    "Authorization", SecurityFrameworkUtils.AUTHORIZATION_BEARER);
        }
        log.info("[getToken][获取到token:{}]", token);
        return CommonResult.success(new WebSocketTestRespVO().setToken(token));
    }
    
    @GetMapping("/check-config")
    @Operation(summary = "检查 WebSocket 配置")
    public CommonResult<Map<String, Object>> checkConfig() {
        Map<String, Object> result = new HashMap<>();
        // 获取WebSocket配置
        result.put("websocketPath", webSocketProperties.getPath());
        result.put("websocketEnable", true);
        result.put("websocketSenderType", webSocketProperties.getSenderType());
        // 返回配置信息
        log.info("[checkConfig][WebSocket配置:{}]", result);
        return CommonResult.success(result);
    }
} 