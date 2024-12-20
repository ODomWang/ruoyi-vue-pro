package cn.iocoder.yudao.module.wenxun.service.draftdata;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo.DraftDataPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo.DraftDataSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.draftdata.DraftDataDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.draftdata.DraftDataMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wenxun.enums.GlobalErrorCodeConstants.DRAFT_DATA_NOT_EXISTS;


/**
 * 文巡-在线检测草稿 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DraftDataServiceImpl implements DraftDataService {

    @Resource
    private DraftDataMapper draftDataMapper;

    @Override
    public Long createDraftData(DraftDataSaveReqVO createReqVO) {
        // 插入
        DraftDataDO draftData = BeanUtils.toBean(createReqVO, DraftDataDO.class);
        draftDataMapper.insert(draftData);
        // 返回
        return draftData.getId();
    }

    @Override
    public void updateDraftData(DraftDataSaveReqVO updateReqVO) {
        // 校验存在
        validateDraftDataExists(updateReqVO.getId());
        // 更新
        DraftDataDO updateObj = BeanUtils.toBean(updateReqVO, DraftDataDO.class);
        draftDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteDraftData(Long id) {
        // 校验存在
        validateDraftDataExists(id);
        // 删除
        draftDataMapper.deleteById(id);
    }

    private void validateDraftDataExists(Long id) {
        if (draftDataMapper.selectById(id) == null) {
            throw exception(DRAFT_DATA_NOT_EXISTS);
        }
    }

    @Override
    public DraftDataDO getDraftData(Long id) {
        return draftDataMapper.selectById(id);
    }

    @Override
    public PageResult<DraftDataDO> getDraftDataPage(DraftDataPageReqVO pageReqVO) {
        return draftDataMapper.selectPage(pageReqVO);
    }

}