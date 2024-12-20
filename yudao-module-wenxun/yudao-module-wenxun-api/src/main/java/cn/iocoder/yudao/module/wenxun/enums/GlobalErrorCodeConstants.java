package cn.iocoder.yudao.module.wenxun.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface GlobalErrorCodeConstants {
    ErrorCode TYPO_CHECKLIST_NOT_EXISTS = new ErrorCode(1_002_007_001, "错词检查不存在");
    ErrorCode DETAIL_CHECK_INFO_NOT_EXISTS = new ErrorCode(2_002_007_001, "检查结果不存在");
    ErrorCode AUDIT_LOG_NOT_EXISTS = new ErrorCode(2_002_007_001, "审核结果不存在");
    ErrorCode DRAFT_DATA_NOT_EXISTS= new ErrorCode(2_002_007_001, "草稿信息不存在");
    ErrorCode  DETAIL_CHECK_AUDIT_INFO_NOT_EXISTS= new ErrorCode(2_002_007_001, "审核记录不存在");
    ErrorCode  CUSTOMER_AUDIT_LOG_NOT_EXISTS= new ErrorCode(2_002_007_001, "审核记录不存在");
}