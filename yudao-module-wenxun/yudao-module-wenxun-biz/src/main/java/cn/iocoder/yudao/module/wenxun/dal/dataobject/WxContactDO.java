package cn.iocoder.yudao.module.wenxun.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信联系人 DO
 */
@TableName("wenxun_wx_contact")
@KeySequence("wenxun_wx_contact_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class WxContactDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 微信ID
     */
    private String wxId;

    /**
     * 好友微信ID
     */
    private String friendWxId;

    /**
     * 微信号
     */
    private String wxCode;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 备注
     */
    private String remark;
    
    /**
     * 标签，多个标签以逗号分隔
     */
    private String tags;
    
    /**
     * 手机号
     */
    private String phoneNumber;
    
    /**
     * 是否星标好友
     */
    private Boolean isStarFriend;
} 