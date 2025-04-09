package cn.iocoder.yudao.module.infra.websocket;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.sender.WebSocketMessageSender;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import cn.iocoder.yudao.module.infra.service.document.DocumentService;
import cn.iocoder.yudao.module.infra.websocket.message.DocumentNotifyMessage;
import cn.iocoder.yudao.module.infra.websocket.message.DocumentUpdateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import jakarta.annotation.Resource;

/**
 * 文档 WebSocket 消息处理器
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class DocumentWebSocketMessageListener implements WebSocketMessageListener<DocumentUpdateMessage> {

    /**
     * WebSocket 消息发送器
     */
    @Resource
    private WebSocketMessageSender webSocketMessageSender;

    /**
     * 文档服务
     */
    @Resource
    private DocumentService documentService;

    @Override
    public void onMessage(WebSocketSession session, DocumentUpdateMessage message) {
        Long userId = WebSocketFrameworkUtils.getLoginUserId(session);
        log.info("[onMessage][收到 ({}) 消息：{}]", userId, message);
        
        // 根据操作类型处理文档
        switch (message.getOperateType()) {
            case DocumentUpdateMessage.OPERATE_TYPE_CREATE_FOLDER: // 创建文件夹
                createFolder(userId, message);
                break;
            case DocumentUpdateMessage.OPERATE_TYPE_CREATE_FILE: // 创建文件
                createFile(userId, message);
                break;
            case DocumentUpdateMessage.OPERATE_TYPE_UPDATE: // 更新文档
                updateDocument(userId, message);
                break;
            case DocumentUpdateMessage.OPERATE_TYPE_DELETE: // 删除文档
                deleteDocument(userId, message);
                break;
            case DocumentUpdateMessage.OPERATE_TYPE_AUTO_SAVE: // 自动保存
                autoSaveDocument(userId, message);
                break;
            case DocumentUpdateMessage.OPERATE_TYPE_UPDATE_STATUS: // 更新状态
                updateDocumentStatus(userId, message);
                break;
            case DocumentUpdateMessage.OPERATE_TYPE_MOVE: // 移动文档
                moveDocument(userId, message);
                break;
            case DocumentUpdateMessage.OPERATE_TYPE_RENAME: // 重命名文档
                renameDocument(userId, message);
                break;
            default:
                log.warn("[onMessage][未知的操作类型：{}]", message.getOperateType());
        }
    }

    /**
     * 创建文件夹
     */
    private void createFolder(Long userId, DocumentUpdateMessage message) {
        // 调用服务创建文件夹
        Long documentId = documentService.createFolder(message.getParentId(), message.getTitle(), userId);
        
        if (documentId != null) {
            // 构建通知消息
            DocumentNotifyMessage notifyMessage = new DocumentNotifyMessage()
                    .setUserId(userId)
                    .setDocumentId(documentId)
                    .setParentId(message.getParentId())
                    .setTitle(message.getTitle())
                    .setType(message.getType())
                    .setStatus(message.getStatus())
                    .setVersion(1)
                    .setOperateType(DocumentUpdateMessage.OPERATE_TYPE_CREATE_FOLDER)
                    .setOperateTime(System.currentTimeMillis());
            
            // 广播消息给所有在线用户
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), "document-notify", notifyMessage);
        }
    }

    /**
     * 创建文件
     */
    private void createFile(Long userId, DocumentUpdateMessage message) {
        // 调用服务创建文件
        Long documentId = documentService.createDocument(message.getParentId(), message.getTitle(), 
                message.getContent(), userId);
        
        if (documentId != null) {
            // 构建通知消息
            DocumentNotifyMessage notifyMessage = new DocumentNotifyMessage()
                    .setUserId(userId)
                    .setDocumentId(documentId)
                    .setParentId(message.getParentId())
                    .setTitle(message.getTitle())
                    .setContent(message.getContent())
                    .setType(message.getType())
                    .setStatus(message.getStatus())
                    .setVersion(1)
                    .setOperateType(DocumentUpdateMessage.OPERATE_TYPE_CREATE_FILE)
                    .setOperateTime(System.currentTimeMillis());
            
            // 广播消息给所有在线用户
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), "document-notify", notifyMessage);
        }
    }

    /**
     * 更新文档
     */
    private void updateDocument(Long userId, DocumentUpdateMessage message) {
        // 调用服务更新文档
        boolean success = documentService.updateDocument(message.getDocumentId(), message.getTitle(), 
                message.getContent(), message.getVersion(), userId);
        
        if (success) {
            // 构建通知消息
            DocumentNotifyMessage notifyMessage = new DocumentNotifyMessage()
                    .setUserId(userId)
                    .setDocumentId(message.getDocumentId())
                    .setParentId(message.getParentId())
                    .setTitle(message.getTitle())
                    .setContent(message.getContent())
                    .setType(message.getType())
                    .setStatus(message.getStatus())
                    .setVersion(message.getVersion() + 1)
                    .setOperateType(DocumentUpdateMessage.OPERATE_TYPE_UPDATE)
                    .setOperateTime(System.currentTimeMillis());
            
            // 广播消息给所有在线用户
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), "document-notify", notifyMessage);
        }
    }

    /**
     * 删除文档
     */
    private void deleteDocument(Long userId, DocumentUpdateMessage message) {
        // 调用服务删除文档
        boolean success = documentService.deleteDocument(message.getDocumentId());
        
        if (success) {
            // 构建通知消息
            DocumentNotifyMessage notifyMessage = new DocumentNotifyMessage()
                    .setUserId(userId)
                    .setDocumentId(message.getDocumentId())
                    .setParentId(message.getParentId())
                    .setOperateType(DocumentUpdateMessage.OPERATE_TYPE_DELETE)
                    .setOperateTime(System.currentTimeMillis());
            
            // 广播消息给所有在线用户
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), "document-notify", notifyMessage);
        }
    }

    /**
     * 自动保存文档
     */
    private void autoSaveDocument(Long userId, DocumentUpdateMessage message) {
        // 调用服务自动保存文档
        boolean success = documentService.autoSaveDocument(message.getDocumentId(), 
                message.getContent(), userId);
        
        if (success) {
            // 构建通知消息
            DocumentNotifyMessage notifyMessage = new DocumentNotifyMessage()
                    .setUserId(userId)
                    .setDocumentId(message.getDocumentId())
                    .setParentId(message.getParentId())
                    .setContent(message.getContent())
                    .setOperateType(DocumentUpdateMessage.OPERATE_TYPE_AUTO_SAVE)
                    .setOperateTime(System.currentTimeMillis());
            
            // 广播消息给所有在线用户
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), "document-notify", notifyMessage);
        }
    }

    /**
     * 更新文档状态
     */
    private void updateDocumentStatus(Long userId, DocumentUpdateMessage message) {
        // 调用服务更新文档状态
        boolean success = documentService.updateDocumentStatus(message.getDocumentId(), 
                message.getStatus(), userId);
        
        if (success) {
            // 构建通知消息
            DocumentNotifyMessage notifyMessage = new DocumentNotifyMessage()
                    .setUserId(userId)
                    .setDocumentId(message.getDocumentId())
                    .setParentId(message.getParentId())
                    .setStatus(message.getStatus())
                    .setOperateType(DocumentUpdateMessage.OPERATE_TYPE_UPDATE_STATUS)
                    .setOperateTime(System.currentTimeMillis());
            
            // 广播消息给所有在线用户
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), "document-notify", notifyMessage);
        }
    }

    /**
     * 移动文档
     */
    private void moveDocument(Long userId, DocumentUpdateMessage message) {
        // 调用服务移动文档
        boolean success = documentService.moveDocument(message.getDocumentId(), 
                message.getParentId(), userId);
        
        if (success) {
            // 构建通知消息
            DocumentNotifyMessage notifyMessage = new DocumentNotifyMessage()
                    .setUserId(userId)
                    .setDocumentId(message.getDocumentId())
                    .setParentId(message.getParentId())
                    .setOperateType(DocumentUpdateMessage.OPERATE_TYPE_MOVE)
                    .setOperateTime(System.currentTimeMillis());
            
            // 广播消息给所有在线用户
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), "document-notify", notifyMessage);
        }
    }

    /**
     * 重命名文档
     */
    private void renameDocument(Long userId, DocumentUpdateMessage message) {
        // 调用服务重命名文档
        boolean success = documentService.renameDocument(message.getDocumentId(), 
                message.getTitle(), userId);
        
        if (success) {
            // 构建通知消息
            DocumentNotifyMessage notifyMessage = new DocumentNotifyMessage()
                    .setUserId(userId)
                    .setDocumentId(message.getDocumentId())
                    .setParentId(message.getParentId())
                    .setTitle(message.getTitle())
                    .setOperateType(DocumentUpdateMessage.OPERATE_TYPE_RENAME)
                    .setOperateTime(System.currentTimeMillis());
            
            // 广播消息给所有在线用户
            webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), "document-notify", notifyMessage);
        }
    }

    @Override
    public String getType() {
        return "document-update";
    }
} 