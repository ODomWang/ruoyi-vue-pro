package cn.iocoder.yudao.module.wenxun.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 微信用户 DO
 */
@TableName("wenxun_wx_user")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WxUserDO extends BaseDO {

    /**
     * 用户编号
     */
    @TableId
    private Long id;

    /**
     * 微信 openid
     */
    private String openid;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 用户状态
     */
    private Integer status;
} 