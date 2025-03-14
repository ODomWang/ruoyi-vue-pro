package cn.iocoder.yudao.module.system.dal.dataobject.urlpinglog;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

/**
 * 网页连通记录-日志 DO
 *
 * @author 文巡智检
 */
@TableName("wenxun_url_ping_log")
@KeySequence("wenxun_url_ping_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
 @ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlPingLogDO {

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
     * 主键，同连通表主键相同
     */
    private Integer pingId;
    /**
     * 状态（1正常 0停用）
     */
    private Integer status;

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
     * 更新者，目前使用 SysUser 的 id 编号
     * <p>
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updater;

    private String pingCode;

    private String url;
}