package cn.iocoder.yudao.module.wenxun.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 文巡 错误码枚举类
 */
public interface ErrorCodeConstants {

    // ========== 用户相关 1-001-000-000 ==========
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1_001_000_000, "用户不存在");
    ErrorCode USER_APP_ID_EXISTS = new ErrorCode(1_001_000_001, "用户应用ID已存在");

    // ========== AUTH 模块 ==========
    ErrorCode AUTH_WEIXIN_CODE_ERROR = new ErrorCode(1001001000, "微信授权码不正确");
    ErrorCode AUTH_USER_DISABLED = new ErrorCode(1001001001, "用户已被禁用");

} 