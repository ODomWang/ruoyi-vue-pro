package cn.iocoder.yudao.module.system.dal.dataobject.draftdata;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 文巡-在线检测草稿 DO
 *
 * @author 芋道源码
 */
@TableName("wenxun_draft_data")
@KeySequence("wenxun_draft_data_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DraftDataDO extends BaseDO {

    /**
     * 草稿id
     */
    @TableId
    private Long id;
    /**
     * 草稿详情
     */
    private String content;
    /**
     * 备注
     */
    private String remark;

}