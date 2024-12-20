package cn.iocoder.yudao.module.wenxun.dal.dataobject.urlpinginfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 * 网页连通记录 DO
 *
 * @author 文巡智检
 */
@TableName("wenxun_url_ping_info")
@KeySequence("wenxun_url_ping_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
 @ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlPingInfoDO  {

    /**
     * 主键，同爬虫配置表主键相同
     */
    @TableId
    private Integer id;
    /**
     * 地址名称
     */
    private String urlName;
    /**
     * 总检查次数
     */
    private Integer allCount;
    /**
     * 连通成功次数
     */
    private Integer successCount;
    /**
     * 异常检查次数
     */
    private Integer failCount;

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

    private String url;



}