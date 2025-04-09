package cn.iocoder.yudao.module.infra.dal.dataobject.document;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 文档分享 DO
 * 
 * @author 芋道源码
 */
@TableName("infra_document_share")
@KeySequence("infra_document_share_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DocumentShareDO extends BaseDO {

    /**
     * 分享编号
     */
    @TableId
    private Long id;
    
    /**
     * 分享链接的标识 UUID
     */
    private String shareId;
    
    /**
     * 文档编号
     */
    private Long documentId;
    
    /**
     * 创建人的用户编号
     */
    private Long userId;
    
    /**
     * 分享密码，允许为空
     */
    private String password;
    
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 分享状态：0-正常，1-已过期，2-已禁用
     */
    private Integer status;
    
    /**
     * 是否需要密码访问：0-不需要，1-需要
     */
    private Boolean passwordProtected;
    
    /**
     * 备注
     */
    private String remark;
} 