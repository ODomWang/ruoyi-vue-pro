package cn.iocoder.yudao.module.infra.websocket.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文档通知消息 - 服务端发送给客户端
 *
 * @author 芋道源码
 */
@Data
@Accessors(chain = true)
public class DocumentNotifyMessage {

    /**
     * 操作用户编号
     */
    private Long userId;
    
    /**
     * 用户昵称
     */
    private String userNickname;
    
    /**
     * 文档编号
     */
    private Long documentId;
    
    /**
     * 父级文档编号
     */
    private Long parentId;
    
    /**
     * 文档标题
     */
    private String title;
    
    /**
     * 文档内容
     */
    private String content;
    
    /**
     * 文档类型：1-文件夹，2-文件
     */
    private Integer type;
    
    /**
     * 文档状态
     */
    private Integer status;
    
    /**
     * 版本号
     */
    private Integer version;
    
    /**
     * 操作类型：1-创建, 2-更新, 3-删除
     */
    private Integer operateType;
    
    /**
     * 操作时间戳
     */
    private Long operateTime;

} 