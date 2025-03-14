package cn.iocoder.yudao.module.system.dal.dataobject.auditlog;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 人工研判审核 DO
 *
 * @author 芋道源码
 */
@TableName("wenxun_audit_log")
@KeySequence("wenxun_audit_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDO extends BaseDO {

    /**
     * 主键
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

    private Long deptId;


}