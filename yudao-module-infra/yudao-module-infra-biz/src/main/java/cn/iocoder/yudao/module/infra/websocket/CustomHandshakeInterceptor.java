package cn.iocoder.yudao.module.infra.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 自定义WebSocket握手拦截器
 * 用于调试WebSocket握手过程，记录详细日志
 */
@Component
@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
                                  WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 打印详细的请求信息
        log.info("[beforeHandshake][握手请求] URL={}, 方法={}, 头部={}", 
                request.getURI(), request.getMethod(), request.getHeaders());
        
        String token = getTokenFromUri(request.getURI().toString());
        log.info("[beforeHandshake][Token] token={}", token);
        
        // 始终返回true，允许连接继续，但记录详细日志用于调试
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, 
                              WebSocketHandler wsHandler, Exception exception) {
        // 记录握手结果
        if (exception != null) {
            log.error("[afterHandshake][握手失败] URL={}, 错误信息={}", 
                    request.getURI(), exception.getMessage(), exception);
        } else {
            log.info("[afterHandshake][握手成功] URL={}, 响应头={}", 
                    request.getURI(), response.getHeaders());
        }
    }
    
    /**
     * 从URI中提取token参数
     */
    private String getTokenFromUri(String uri) {
        int tokenIndex = uri.indexOf("token=");
        if (tokenIndex != -1) {
            String tokenPart = uri.substring(tokenIndex + 6);
            int endIndex = tokenPart.indexOf("&");
            if (endIndex != -1) {
                return tokenPart.substring(0, endIndex);
            }
            return tokenPart;
        }
        return "";
    }
} 