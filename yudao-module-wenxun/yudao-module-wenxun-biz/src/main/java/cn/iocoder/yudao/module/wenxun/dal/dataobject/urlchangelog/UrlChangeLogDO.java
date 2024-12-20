package cn.iocoder.yudao.module.wenxun.dal.dataobject.urlchangelog;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 网站更新检查-日志 DO
 *
 * @author 文巡一哥
 */
@TableName("wenxun_url_change_log")
@KeySequence("wenxun_url_change_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
 @ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlChangeLogDO  {

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
     * 主键，同爬虫主键相同
     */
    private Integer spiderId;
    /**
     * 状态（1正常 0停用）
     */
    private Integer status;
    /**
     * 标题
     */
    private String title;
    /**
     * url
     */
    private String url;

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
    /**
     * 最后更新时间
     */
     private String  updater;



}