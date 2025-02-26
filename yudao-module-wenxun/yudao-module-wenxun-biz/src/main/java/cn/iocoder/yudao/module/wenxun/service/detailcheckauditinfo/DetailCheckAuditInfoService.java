package cn.iocoder.yudao.module.wenxun.service.detailcheckauditinfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckauditinfo.DetailCheckAuditInfoDO;

import jakarta.validation.Valid;

/**
 * 详情检测信息表-用户审核 Service 接口
 *
 * @author 文巡智检
 */
public interface DetailCheckAuditInfoService {

    /**
     * 创建详情检测信息表-用户审核
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDetailCheckAuditInfo(@Valid DetailCheckAuditInfoSaveReqVO createReqVO);

    /**
     * 更新详情检测信息表-用户审核
     *
     * @param updateReqVO 更新信息
     */
    void updateDetailCheckAuditInfo(@Valid DetailCheckAuditInfoSaveReqVO updateReqVO);

    /**
     * 删除详情检测信息表-用户审核
     *
     * @param id 编号
     */
    void deleteDetailCheckAuditInfo(Long id);

    /**
     * 获得详情检测信息表-用户审核
     *
     * @param id 编号
     * @return 详情检测信息表-用户审核
     */
    DetailCheckAuditInfoDO getDetailCheckAuditInfo(Long id);

    /**
     * 获得详情检测信息表-用户审核分页
     *
     * @param pageReqVO 分页查询
     * @return 详情检测信息表-用户审核分页
     */
    PageResult<DetailCheckAuditInfoDO> getDetailCheckAuditInfoPage(DetailCheckAuditInfoPageReqVO pageReqVO);

}