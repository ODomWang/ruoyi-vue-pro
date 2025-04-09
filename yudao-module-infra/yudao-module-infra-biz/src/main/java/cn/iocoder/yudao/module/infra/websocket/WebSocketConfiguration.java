package cn.iocoder.yudao.module.infra.websocket;

import cn.iocoder.yudao.framework.websocket.config.WebSocketProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;

/**
 * WebSocket诊断配置类
 * 用于添加诊断日志和改善WebSocket握手失败的诊断
 * 显式配置WebSocket路径和处理器
 */
@Configuration
@EnableWebSocket
@Slf4j
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final WebSocketProperties webSocketProperties;
    private final WebSocketHandler webSocketHandler;
    private final HandshakeInterceptor[] handshakeInterceptors;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 打印诊断信息
        log.info("[WebSocketConfiguration][初始化] WebSocket配置: 路径={}, 发送器类型={}", 
                webSocketProperties.getPath(), webSocketProperties.getSenderType());
        
        log.info("[WebSocketConfiguration][初始化] WebSocket处理器: {}", webSocketHandler.getClass().getName());
        
        log.info("[WebSocketConfiguration][初始化] WebSocket拦截器: {}", 
                Arrays.stream(handshakeInterceptors)
                    .map(interceptor -> interceptor.getClass().getSimpleName())
                    .toList());
        
        // 显式注册WebSocket处理器
        registry.addHandler(webSocketHandler, webSocketProperties.getPath())
                .addInterceptors(handshakeInterceptors)
                .setAllowedOrigins("*"); // 允许所有来源的跨域请求
                
        log.info("[WebSocketConfiguration][注册完成] WebSocket处理器已注册到路径: {}", webSocketProperties.getPath());
    }
} 