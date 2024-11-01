package cn.iocoder.yudao.module.wenxun.service.detailcheckinfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.DetailCheckInfoPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.DetailCheckInfoSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.detailcheckinfo.DetailCheckInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wenxun.enums.GlobalErrorCodeConstants.DETAIL_CHECK_INFO_NOT_EXISTS;


/**
 * 详情检测信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DetailCheckInfoServiceImpl implements DetailCheckInfoService {

    @Resource
    private DetailCheckInfoMapper detailCheckInfoMapper;

    @Override
    public Long createDetailCheckInfo(DetailCheckInfoSaveReqVO createReqVO) {
        // 插入
        DetailCheckInfoDO detailCheckInfo = BeanUtils.toBean(createReqVO, DetailCheckInfoDO.class);
        DetailCheckInfoDO detailCheckInfo2 = detailCheckInfoMapper.selectOne("sourceUrl", detailCheckInfo.getSourceUrl());
        if (detailCheckInfo2 != null) {
            detailCheckInfo.setId(detailCheckInfo2.getId());
            detailCheckInfo.setCreateTime(detailCheckInfo2.getCreateTime());
        }
        detailCheckInfoMapper.insert(detailCheckInfo);
        // 返回
        return detailCheckInfo.getId();
    }

    @Override
    public void updateDetailCheckInfo(DetailCheckInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateDetailCheckInfoExists(updateReqVO.getId());
        // 更新
        DetailCheckInfoDO updateObj = BeanUtils.toBean(updateReqVO, DetailCheckInfoDO.class);
        detailCheckInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteDetailCheckInfo(Long id) {
        // 校验存在
        validateDetailCheckInfoExists(id);
        // 删除
        detailCheckInfoMapper.deleteById(id);
    }

    private void validateDetailCheckInfoExists(Long id) {
        if (detailCheckInfoMapper.selectById(id) == null) {
            throw exception(DETAIL_CHECK_INFO_NOT_EXISTS);

        }
    }

    @Override
    public DetailCheckInfoDO getDetailCheckInfo(Long id) {
        return detailCheckInfoMapper.selectById(id);
    }

    @Override
    public PageResult<DetailCheckInfoDO> getDetailCheckInfoPage(DetailCheckInfoPageReqVO pageReqVO) {
        return detailCheckInfoMapper.selectPage(pageReqVO);
    }

}