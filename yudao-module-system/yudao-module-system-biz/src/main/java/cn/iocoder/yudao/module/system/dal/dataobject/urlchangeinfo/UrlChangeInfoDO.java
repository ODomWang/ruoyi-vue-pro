package cn.iocoder.yudao.module.system.dal.dataobject.urlchangeinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 * 网站更新检查 DO
 *
 * @author 文巡一哥
 */
@TableName("wenxun_url_change_info")
@KeySequence("wenxun_url_change_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
 @ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlChangeInfoDO  extends  BaseDO {

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
     * 更新天数
     */
    private Integer successCount;
    /**
     * 未更新天数
     */
    private Integer failCount;
    /**
     * url
     */
    private String url;
    /**
     * 最后标题
     */
    private String lastTitle;
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

    private Long deptId;



}