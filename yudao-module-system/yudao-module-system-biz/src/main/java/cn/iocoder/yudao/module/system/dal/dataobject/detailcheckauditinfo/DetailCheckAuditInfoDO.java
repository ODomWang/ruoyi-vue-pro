package cn.iocoder.yudao.module.system.dal.dataobject.detailcheckauditinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 详情检测信息表-用户审核 DO
 *
 * @author 文巡智检
 */
@TableName("wenxun_detail_check_audit_info")
@KeySequence("wenxun_detail_check_audit_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
 @ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCheckAuditInfoDO  extends BaseDO{

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 检查源，1 敏感词，2，错词，3，接口
     */
    private Integer checkSource;
    /**
     * 错词详情
     */
    private String checkDetail;
    /**
     * 修正词汇
     */
    private String targetDetail;
    /**
     * 数据状态
     */
    private Integer status;
    /**
     * 源网站地址
     */
    private String sourceUrl;
    /**
     * 采集配置id
     */
    private Integer spiderConfigId;
    /**
     * 网站图标
     */
    private String webIcon;
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

    private String titleDesc;

    private Long deptId;


}