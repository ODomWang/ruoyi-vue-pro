package cn.iocoder.yudao.module.wenxun.service.detailcheckinfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.dal.mysql.wenxunDict.WenXunDictDataMapper;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.DetailCheckInfoPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.DetailCheckInfoSaveReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.DetailCheckInfoWithDictDataRespVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.detailcheckinfo.DetailCheckInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

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
    @Resource
    private WenXunDictDataMapper wenXunDictdataMapper;

    @Override
    public Long createDetailCheckInfo(DetailCheckInfoSaveReqVO createReqVO) {
        try {
            // 插入
            DetailCheckInfoDO detailCheckInfo = BeanUtils.toBean(createReqVO, DetailCheckInfoDO.class);
            DetailCheckInfoDO detailCheckInfo2 = detailCheckInfoMapper.selectOne(DetailCheckInfoDO::getSourceUrl, detailCheckInfo.getSourceUrl());
            if (detailCheckInfo2 != null) {
                detailCheckInfo.setId(detailCheckInfo2.getId());
                detailCheckInfo.setCreateTime(detailCheckInfo2.getCreateTime());
            }
            detailCheckInfoMapper.insertOrUpdate(detailCheckInfo);
            // 返回
            return detailCheckInfo.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1L;
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
    public DetailCheckInfoWithDictDataRespVO getDetailCheckInfo(Long id) {
        DetailCheckInfoDO result = detailCheckInfoMapper.selectById(id);
        if (result != null) {
            String[] ids = result.getTargetDetail().split(",");
            List<WenXunDictDataDO> checkInfoDOS = wenXunDictdataMapper.selectBatchIds(Arrays.asList(ids));
            PageResult<WenXunDictDataDO> pageResult1 = new PageResult<>();
            pageResult1.setList(checkInfoDOS);
            pageResult1.setTotal((long) checkInfoDOS.size());
            DetailCheckInfoWithDictDataRespVO vo = BeanUtils.toBean(result, DetailCheckInfoWithDictDataRespVO.class);
            vo.setDictDataDOS(pageResult1);
            return vo;
        }
        return null;
    }

    @Override
    public PageResult<DetailCheckInfoDO> getDetailCheckInfoPage(DetailCheckInfoPageReqVO pageReqVO) {
        PageResult<DetailCheckInfoDO> doPageResult = detailCheckInfoMapper.selectPage(pageReqVO);
        return doPageResult;
    }

    @Override
    public List<DetailCheckInfoDO> selectJoinList() {
        return detailCheckInfoMapper.selectJoinList();
    }


}