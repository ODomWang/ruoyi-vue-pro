package cn.iocoder.yudao.module.infra.websocket.message;

import lombok.Data;

/**
 * 文档更新消息
 */
@Data
public class DocumentUpdateMessage {

    /**
     * 操作类型
     */
    private Integer operateType;

    /**
     * 文档编号
     */
    private Long documentId;

    /**
     * 父文档编号
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
     * 文档类型
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

    // ========== 操作类型 ==========
    public static final int OPERATE_TYPE_CREATE_FOLDER = 1; // 创建文件夹
    public static final int OPERATE_TYPE_CREATE_FILE = 2; // 创建文件
    public static final int OPERATE_TYPE_UPDATE = 3; // 更新文档
    public static final int OPERATE_TYPE_DELETE = 4; // 删除文档
    public static final int OPERATE_TYPE_AUTO_SAVE = 5; // 自动保存
    public static final int OPERATE_TYPE_UPDATE_STATUS = 6; // 更新状态
    public static final int OPERATE_TYPE_MOVE = 7; // 移动文档
    public static final int OPERATE_TYPE_RENAME = 8; // 重命名文档

} 