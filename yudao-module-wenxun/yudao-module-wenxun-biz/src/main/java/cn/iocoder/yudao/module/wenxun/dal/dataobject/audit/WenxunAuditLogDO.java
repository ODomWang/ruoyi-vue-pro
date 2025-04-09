package cn.iocoder.yudao.module.wenxun.dal.dataobject.audit;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 审核日志 DO
 */
@TableName("wenxun_audit_log")
@KeySequence("wenxun_audit_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WenxunAuditLogDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 文章校验表ID
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
     * 0 - 待审核
     * 1 - 审核通过
     * 2 - 审核驳回
     */
    private Integer status;

    /**
     * 操作员名称
     */
    private String creator;

    /**
     * 部门ID
     */
    private Long deptId;
} 