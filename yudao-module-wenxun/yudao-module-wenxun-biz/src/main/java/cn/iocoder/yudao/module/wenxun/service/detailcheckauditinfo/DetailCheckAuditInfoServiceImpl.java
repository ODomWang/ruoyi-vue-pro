package cn.iocoder.yudao.module.wenxun.service.detailcheckauditinfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckauditinfo.DetailCheckAuditInfoDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.detailcheckauditinfo.DetailCheckAuditInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wenxun.enums.GlobalErrorCodeConstants.DETAIL_CHECK_AUDIT_INFO_NOT_EXISTS;

/**
 * 详情检测信息表-用户审核 Service 实现类
 *
 * @author 文巡智检
 */
@Service
@Validated
public class DetailCheckAuditInfoServiceImpl implements DetailCheckAuditInfoService {

    @Resource
    private DetailCheckAuditInfoMapper detailCheckAuditInfoMapper;

    @Override
    public Long createDetailCheckAuditInfo(DetailCheckAuditInfoSaveReqVO createReqVO) {
        // 插入
        DetailCheckAuditInfoDO detailCheckAuditInfo = BeanUtils.toBean(createReqVO, DetailCheckAuditInfoDO.class);
         detailCheckAuditInfoMapper.insertOrUpdate(detailCheckAuditInfo);
        // 返回
        return detailCheckAuditInfo.getId();
    }

    @Override
    public void updateDetailCheckAuditInfo(DetailCheckAuditInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateDetailCheckAuditInfoExists(updateReqVO.getId());
        // 更新
        DetailCheckAuditInfoDO updateObj = BeanUtils.toBean(updateReqVO, DetailCheckAuditInfoDO.class);
        detailCheckAuditInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteDetailCheckAuditInfo(Long id) {
        // 校验存在
        validateDetailCheckAuditInfoExists(id);
        // 删除
        detailCheckAuditInfoMapper.deleteById(id);
    }

    private void validateDetailCheckAuditInfoExists(Long id) {
        if (detailCheckAuditInfoMapper.selectById(id) == null) {
            throw exception(DETAIL_CHECK_AUDIT_INFO_NOT_EXISTS);
        }
    }

    @Override
    public DetailCheckAuditInfoDO getDetailCheckAuditInfo(Long id) {
        return detailCheckAuditInfoMapper.selectById(id);
    }

    @Override
    public PageResult<DetailCheckAuditInfoDO> getDetailCheckAuditInfoPage(DetailCheckAuditInfoPageReqVO pageReqVO) {
        return detailCheckAuditInfoMapper.selectPage(pageReqVO);
    }

}