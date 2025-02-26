package cn.iocoder.yudao.module.wenxun.service.customerauditlog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.customerauditlog.vo.CustomerAuditLogPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.customerauditlog.vo.CustomerAuditLogSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.customerauditlog.CustomerAuditLogDO;

import jakarta.validation.Valid;

/**
 * 客户审核 Service 接口
 *
 * @author 文巡智检
 */
public interface CustomerAuditLogService {

    /**
     * 创建客户审核
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createCustomerAuditLog(@Valid CustomerAuditLogSaveReqVO createReqVO);

    /**
     * 更新客户审核
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomerAuditLog(@Valid CustomerAuditLogSaveReqVO updateReqVO);

    /**
     * 删除客户审核
     *
     * @param id 编号
     */
    void deleteCustomerAuditLog(Integer id);

    /**
     * 获得客户审核
     *
     * @param id 编号
     * @return 客户审核
     */
    CustomerAuditLogDO getCustomerAuditLog(Integer id);

    /**
     * 获得客户审核分页
     *
     * @param pageReqVO 分页查询
     * @return 客户审核分页
     */
    PageResult<CustomerAuditLogDO> getCustomerAuditLogPage(CustomerAuditLogPageReqVO pageReqVO);

}