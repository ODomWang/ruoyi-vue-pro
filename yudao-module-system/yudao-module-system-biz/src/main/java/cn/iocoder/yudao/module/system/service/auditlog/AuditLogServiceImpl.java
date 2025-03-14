package cn.iocoder.yudao.module.system.service.auditlog;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.controller.admin.auditlog.vo.AuditLogSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.AuditLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.auditlog.AuditLogDO;
import cn.iocoder.yudao.module.system.dal.dataobject.customerauditlog.CustomerAuditLogDO;
import cn.iocoder.yudao.module.system.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import cn.iocoder.yudao.module.system.dal.mysql.auditlog.AuditLogMapper;
import cn.iocoder.yudao.module.system.dal.mysql.customerauditlog.CustomerAuditLogMapper;
import cn.iocoder.yudao.module.system.dal.mysql.detailcheckauditinfo.DetailCheckAuditInfoMapper;
import cn.iocoder.yudao.module.system.dal.mysql.detailcheckinfo.DetailCheckInfoMapper;
import cn.iocoder.yudao.module.system.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.module.system.service.detailcheckauditinfo.DetailCheckAuditInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;


/**
 * 人工研判审核 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AuditLogServiceImpl implements AuditLogService {

    @Resource
    private AuditLogMapper auditLogMapper;
    @Resource
    private DetailCheckAuditInfoService detailCheckAuditInfoService;
    @Resource
    private DetailCheckInfoMapper detailCheckInfoMapper;
    @Resource
    private DetailCheckAuditInfoMapper detailCheckAuditInfoMapper;
    @Resource
    private CustomerAuditLogMapper customerAuditLogMapper;

    @Override
    public Integer createAuditLog(AuditLogSaveReqVO createReqVO) {
        // 插入
        try {
            AuditLogDO auditLog = BeanUtils.toBean(createReqVO, AuditLogDO.class);
            auditLog.setCreator("System");
            auditLog.setUpdater("System");

            auditLogMapper.insert(auditLog);
            // 返回
            return auditLog.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAuditLog(AuditLogSaveReqVO updateReqVO) {
        // 校验存在
        AuditLogDO updateObj = auditLogMapper.selectOne(AuditLogDO::getSpiderId, updateReqVO.getSpiderId());
        if (updateObj == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.AUDIT_LOG_NOT_EXISTS);
        }
        // 更新 合并入参但是跳过null
        BeanUtil.copyProperties(updateReqVO, updateObj, CopyOptions.create().setIgnoreNullValue(true));
        auditLogMapper.updateById(updateObj);
        // 审核通过数据加入到用户审核页面 只有审核通过才进入客户表
        if (updateObj.getStatus() == 1) {
            // 查询原文数据
            DetailCheckInfoDO infoDO = detailCheckInfoMapper.selectById(updateObj.getSpiderId());
            //合并审核数据和状态
            if (infoDO != null) {
                // 初始化审核通过状态
                infoDO.setStatus(0);
                infoDO.setTargetDetail(updateObj.getApprovedRecord());
                DetailCheckAuditInfoSaveReqVO vo = new DetailCheckAuditInfoSaveReqVO();
                BeanUtil.copyProperties(infoDO, vo);
                detailCheckAuditInfoService.createDetailCheckAuditInfo(vo);

                CustomerAuditLogDO customerAuditLogDO = customerAuditLogMapper.selectOne(CustomerAuditLogDO::getSpiderId, vo.getId());

                if (customerAuditLogDO == null) {
                    customerAuditLogDO = new CustomerAuditLogDO();
                    customerAuditLogDO.setSpiderId(vo.getId().toString());
                    customerAuditLogDO.setApprovedRecord(null); // 占位，必要时填充
                    customerAuditLogDO.setRejectedRecord(null); // 占位，必要时填充
                    customerAuditLogDO.setDeptId(infoDO.getDeptId());
                    customerAuditLogDO.setCreator(SecurityFrameworkUtils.getLoginUserNickname());
                    customerAuditLogDO.setCreateTime(LocalDateTime.now());

                }
                customerAuditLogDO.setUpdateTime(LocalDateTime.now());
                customerAuditLogDO.setUpdater(SecurityFrameworkUtils.getLoginUserNickname());
                customerAuditLogDO.setStatus(0); // 默认状态
                customerAuditLogDO.setAuditor("system"); // 默认操作员
                customerAuditLogMapper.insertOrUpdate(customerAuditLogDO);
            }

        }
    }

    @Override
    public void deleteAuditLog(Integer id) {
        // 校验存在
        validateAuditLogExists(id);
        // 删除
        auditLogMapper.deleteById(id);
    }

    private void validateAuditLogExists(Integer id) {
        if (auditLogMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.AUDIT_LOG_NOT_EXISTS);

        }
    }

    @Override
    public AuditLogDO getAuditLog(Integer id) {
        return auditLogMapper.selectById(id);
    }

    @Override
    public PageResult<AuditLogDO> getAuditLogPage(AuditLogPageReqVO pageReqVO) {
        return auditLogMapper.selectPage(pageReqVO);
    }

}