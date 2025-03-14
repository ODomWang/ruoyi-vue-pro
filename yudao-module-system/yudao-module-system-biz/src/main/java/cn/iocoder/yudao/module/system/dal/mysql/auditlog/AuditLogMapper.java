package cn.iocoder.yudao.module.system.dal.mysql.auditlog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.AuditLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.auditlog.AuditLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人工研判审核 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AuditLogMapper extends BaseMapperX<AuditLogDO> {

    default PageResult<AuditLogDO> selectPage(AuditLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AuditLogDO>()
                .eqIfPresent(AuditLogDO::getSpiderId, reqVO.getSpiderId())
                .eqIfPresent(AuditLogDO::getApprovedRecord, reqVO.getApprovedRecord())
                .eqIfPresent(AuditLogDO::getRejectedRecord, reqVO.getRejectedRecord())
                .eqIfPresent(AuditLogDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AuditLogDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AuditLogDO::getUpdater, reqVO.getUpdater())
                .orderByDesc(AuditLogDO::getId));
    }

}