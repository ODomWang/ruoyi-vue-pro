package cn.iocoder.yudao.module.infra.websocket;

import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket调试监听器
 * 用于记录WebSocket消息和会话管理事件
 */
@Component
@Slf4j
public class WebSocketDebugListener implements WebSocketMessageListener<Object> {

    @Override
    public void onMessage(WebSocketSession session, Object message) {
        log.info("[onMessage][会话({}) 收到消息: {}]", session.getId(), message);
    }

    @Override
    public String getType() {
        // 仅记录调试日志，不实际处理任何特定类型的消息
        return "debug-listener";
    }
} 