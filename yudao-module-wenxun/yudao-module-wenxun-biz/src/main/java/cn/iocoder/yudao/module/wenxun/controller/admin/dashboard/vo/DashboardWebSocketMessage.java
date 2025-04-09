package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo;

import lombok.Data;

/**
 * WebSocket 消息实体
 */
@Data
public class DashboardWebSocketMessage {

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息数据
     */
    private Object data;

    /**
     * 构造函数
     *
     * @param type 消息类型
     * @param data 消息数据
     */
    public DashboardWebSocketMessage(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    /**
     * 构造概览更新消息
     *
     * @param data 概览数据
     * @return WebSocket 消息
     */
    public static DashboardWebSocketMessage overviewUpdate(Object data) {
        return new DashboardWebSocketMessage("overview-update", data);
    }

    /**
     * 构造新文档通知消息
     *
     * @param data 新文档数据
     * @return WebSocket 消息
     */
    public static DashboardWebSocketMessage newDocument(Object data) {
        return new DashboardWebSocketMessage("new-document", data);
    }

    /**
     * 构造新错误通知消息
     *
     * @param data 新错误数据
     * @return WebSocket 消息
     */
    public static DashboardWebSocketMessage newError(Object data) {
        return new DashboardWebSocketMessage("new-error", data);
    }

    /**
     * 构造网站状态变更通知消息
     *
     * @param data 网站状态数据
     * @return WebSocket 消息
     */
    public static DashboardWebSocketMessage siteStatusChange(Object data) {
        return new DashboardWebSocketMessage("site-status-change", data);
    }
} 