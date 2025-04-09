package cn.iocoder.yudao.module.infra.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Infra 错误码枚举类
 *
 * infra 系统，使用 1-001-000-000 段
 */
public interface ErrorCodeConstantsDocument {

    // ========== 文档分享 1-001-001-000 ==========
    ErrorCode DOCUMENT_SHARE_NOT_EXISTS = new ErrorCode(1_001_001_000, "文档分享不存在");
    ErrorCode DOCUMENT_SHARE_PASSWORD_ERROR = new ErrorCode(1_001_001_001, "文档分享密码错误");
    ErrorCode DOCUMENT_SHARE_EXPIRED = new ErrorCode(1_001_001_002, "文档分享已过期");
    ErrorCode DOCUMENT_SHARE_DISABLED = new ErrorCode(1_001_001_003, "文档分享已禁用");
    
} 