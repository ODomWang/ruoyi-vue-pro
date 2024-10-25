package cn.iocoder.yudao.module.wenxun.dal.dataobject.typochecklist;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 错词检查 DO
 *
 * @author 芋道源码
 */
@TableName("wenxun_typo_checklist")
@KeySequence("wenxun_typo_checklist_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypoChecklistDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 异常词汇
     */
    private String typo;
    /**
     * 校政词汇
     */
    private String correction;
    /**
     * 数据状态。0未修复，1已修复，2不存在，3.无需处理
     */
    private Integer status;
    /**
     * 文章地址
     */
    private String spiderUrl;
    /**
     * 错词等级
     */
    private String colorType;

}