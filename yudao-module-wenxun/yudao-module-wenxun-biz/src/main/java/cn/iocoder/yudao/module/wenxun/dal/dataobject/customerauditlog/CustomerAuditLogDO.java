package cn.iocoder.yudao.module.wenxun.dal.dataobject.customerauditlog;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 客户审核 DO
 *
 * @author 文巡智检
 */
@TableName("wenxun_customer_audit_log")
@KeySequence("wenxun_customer_audit_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAuditLogDO  {

    /**
     * 住建
     */
    @TableId
    private Integer id;
    /**
     * 文章校验表id
     */
    private String spiderId;
    /**
     * 审核通过信息
     */
    private String approvedRecord;
    /**
     * 审核驳回信息
     */
    private String rejectedRecord;
    /**
     * 审核状态
     */
    private Integer status;
    /**
     * 操作员名称
     */
    private String auditor;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}