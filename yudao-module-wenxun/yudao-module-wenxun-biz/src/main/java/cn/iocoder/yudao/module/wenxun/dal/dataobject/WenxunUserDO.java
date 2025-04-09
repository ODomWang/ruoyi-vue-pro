package cn.iocoder.yudao.module.wenxun.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 文巡用户 DO
 */
@TableName("wenxun_user")
@KeySequence("wenxun_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WenxunUserDO extends BaseDO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 用户token
     */
    private String token;

    /**
     * 登录状态
     */
    private Integer status;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;
} 