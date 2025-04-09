package cn.iocoder.yudao.module.infra.dal.dataobject.document;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 文档 DO
 * 
 * @author 芋道源码
 */
@TableName("infra_document")
@KeySequence("infra_document_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DocumentDO extends BaseDO {

    /**
     * 根文档编号
     */
    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 文档编号
     */
    @TableId
    private Long id;
    
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
     * 文档类型
     */
    private Integer type;
    
    /**
     * 文档状态
     * 
     * 枚举 {@link cn.iocoder.yudao.module.infra.enums.document.DocumentStatusEnum}
     */
    private Integer status;
    
    /**
     * 最后修改人的用户编号
     */
    private Long lastUpdatedBy;
    
    /**
     * 版本号
     */
    private Integer version;
    
    /**
     * 备注
     */
    private String remark;

} 