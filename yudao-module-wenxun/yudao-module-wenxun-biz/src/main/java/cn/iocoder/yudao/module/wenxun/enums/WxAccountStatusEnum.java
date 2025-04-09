package cn.iocoder.yudao.module.wenxun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信账号 - 状态
 */
@Getter
@AllArgsConstructor
public enum WxAccountStatusEnum {

    ONLINE(1, "在线"),
    OFFLINE(2, "离线");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

} 