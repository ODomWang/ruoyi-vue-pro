package cn.iocoder.yudao.module.wenxun.dal.mysql.customerauditlog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.wenxun.controller.admin.customerauditlog.vo.CustomerAuditLogPageReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.customerauditlog.CustomerAuditLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户审核 Mapper
 *
 * @author 文巡智检
 */
@Mapper
public interface CustomerAuditLogMapper extends BaseMapperX<CustomerAuditLogDO> {

    default PageResult<CustomerAuditLogDO> selectPage(CustomerAuditLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomerAuditLogDO>()
                .eqIfPresent(CustomerAuditLogDO::getSpiderId, reqVO.getSpiderId())
                .eqIfPresent(CustomerAuditLogDO::getApprovedRecord, reqVO.getApprovedRecord())
                .eqIfPresent(CustomerAuditLogDO::getRejectedRecord, reqVO.getRejectedRecord())
                .eqIfPresent(CustomerAuditLogDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CustomerAuditLogDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CustomerAuditLogDO::getAuditor, reqVO.getAuditor())
                .orderByDesc(CustomerAuditLogDO::getId));
    }

}