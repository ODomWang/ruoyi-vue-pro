package cn.iocoder.yudao.module.wenxun.service.customerauditlog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.wenxun.controller.admin.customerauditlog.vo.CustomerAuditLogPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.customerauditlog.vo.CustomerAuditLogSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.customerauditlog.CustomerAuditLogDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.customerauditlog.CustomerAuditLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wenxun.enums.GlobalErrorCodeConstants.CUSTOMER_AUDIT_LOG_NOT_EXISTS;


/**
 * 客户审核 Service 实现类
 *
 * @author 文巡智检
 */
@Service
@Validated
public class CustomerAuditLogServiceImpl implements CustomerAuditLogService {

    @Resource
    private CustomerAuditLogMapper customerAuditLogMapper;

    @Override
    public Integer createCustomerAuditLog(CustomerAuditLogSaveReqVO createReqVO) {
        // 插入
        CustomerAuditLogDO customerAuditLog = BeanUtils.toBean(createReqVO, CustomerAuditLogDO.class);
        customerAuditLogMapper.insert(customerAuditLog);
        // 返回
        return customerAuditLog.getId();
    }

    @Override
    public void updateCustomerAuditLog(CustomerAuditLogSaveReqVO updateReqVO) {
        // 校验存在
        validateCustomerAuditLogExists(updateReqVO.getId());
        // 更新
        CustomerAuditLogDO updateObj = BeanUtils.toBean(updateReqVO, CustomerAuditLogDO.class);
        customerAuditLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteCustomerAuditLog(Integer id) {
        // 校验存在
        validateCustomerAuditLogExists(id);
        // 删除
        customerAuditLogMapper.deleteById(id);
    }

    private void validateCustomerAuditLogExists(Integer id) {
        if (customerAuditLogMapper.selectById(id) == null) {
            throw exception(CUSTOMER_AUDIT_LOG_NOT_EXISTS);
        }
    }

    @Override
    public CustomerAuditLogDO getCustomerAuditLog(Integer id) {
        return customerAuditLogMapper.selectById(id);
    }

    @Override
    public PageResult<CustomerAuditLogDO> getCustomerAuditLogPage(CustomerAuditLogPageReqVO pageReqVO) {
        return customerAuditLogMapper.selectPage(pageReqVO);
    }

}