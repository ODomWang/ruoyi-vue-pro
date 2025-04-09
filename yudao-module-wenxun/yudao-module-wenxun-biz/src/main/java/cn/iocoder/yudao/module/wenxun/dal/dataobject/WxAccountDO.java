package cn.iocoder.yudao.module.wenxun.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信账号 DO
 */
@TableName("wenxun_wx_account")
@KeySequence("wenxun_wx_account_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class WxAccountDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * AppID
     */
    private String appId;

    /**
     * 微信ID
     */
    private String wxId;

    /**
     * 状态
     *
     * 枚举 {@link cn.iocoder.yudao.module.wenxun.enums.WxAccountStatusEnum}
     */
    private Integer status;

} 