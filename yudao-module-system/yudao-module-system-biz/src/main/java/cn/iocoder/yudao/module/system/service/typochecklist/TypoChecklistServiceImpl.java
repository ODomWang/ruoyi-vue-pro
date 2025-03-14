package cn.iocoder.yudao.module.system.service.typochecklist;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.typochecklist.vo.TypoChecklistPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.typochecklist.vo.TypoChecklistSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.typochecklist.TypoChecklistDO;
import cn.iocoder.yudao.module.system.dal.mysql.typochecklist.TypoChecklistMapper;
import cn.iocoder.yudao.module.system.enums.GlobalErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/**
 * 错词检查 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TypoChecklistServiceImpl implements TypoChecklistService {

    @Resource
    private TypoChecklistMapper typoChecklistMapper;

    @Override
    public Long createTypoChecklist(TypoChecklistSaveReqVO createReqVO) {
        // 插入
        TypoChecklistDO typoChecklist = BeanUtils.toBean(createReqVO, TypoChecklistDO.class);
        typoChecklistMapper.insert(typoChecklist);
        // 返回
        return typoChecklist.getId();
    }

    @Override
    public void updateTypoChecklist(TypoChecklistSaveReqVO updateReqVO) {
        // 校验存在
        validateTypoChecklistExists(updateReqVO.getId());
        // 更新
        TypoChecklistDO updateObj = BeanUtils.toBean(updateReqVO, TypoChecklistDO.class);
        typoChecklistMapper.updateById(updateObj);
    }

    @Override
    public void deleteTypoChecklist(Long id) {
        // 校验存在
        validateTypoChecklistExists(id);
        // 删除
        typoChecklistMapper.deleteById(id);
    }

    private void validateTypoChecklistExists(Long id) {
        if (typoChecklistMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.TYPO_CHECKLIST_NOT_EXISTS);
        }
    }

    @Override
    public TypoChecklistDO getTypoChecklist(Long id) {
        return typoChecklistMapper.selectById(id);
    }

    @Override
    public PageResult<TypoChecklistDO> getTypoChecklistPage(TypoChecklistPageReqVO pageReqVO) {
        return typoChecklistMapper.selectPage(pageReqVO);
    }

}