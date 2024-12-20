package cn.iocoder.yudao.module.wenxun.service.auditlog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.auditlog.vo.AuditLogPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.auditlog.vo.AuditLogSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.auditlog.AuditLogDO;

import javax.validation.Valid;

/**
 * 人工研判审核 Service 接口
 *
 * @author 芋道源码
 */
public interface AuditLogService {

    /**
     * 创建人工研判审核
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createAuditLog(@Valid AuditLogSaveReqVO createReqVO);

    /**
     * 更新人工研判审核
     *
     * @param updateReqVO 更新信息
     */
    void updateAuditLog(@Valid AuditLogSaveReqVO updateReqVO);

    /**
     * 删除人工研判审核
     *
     * @param id 编号
     */
    void deleteAuditLog(Integer id);

    /**
     * 获得人工研判审核
     *
     * @param id 编号
     * @return 人工研判审核
     */
    AuditLogDO getAuditLog(Integer id);

    /**
     * 获得人工研判审核分页
     *
     * @param pageReqVO 分页查询
     * @return 人工研判审核分页
     */
    PageResult<AuditLogDO> getAuditLogPage(AuditLogPageReqVO pageReqVO);

}