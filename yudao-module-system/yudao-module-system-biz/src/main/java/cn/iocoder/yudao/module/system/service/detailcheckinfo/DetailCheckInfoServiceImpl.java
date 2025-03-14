package cn.iocoder.yudao.module.system.service.detailcheckinfo;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoWithDictDataRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import cn.iocoder.yudao.module.system.dal.mysql.detailcheckinfo.DetailCheckInfoMapper;
import cn.iocoder.yudao.module.system.dal.mysql.wenxunDict.WenXunDictDataMapper;
import cn.iocoder.yudao.module.system.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.module.system.model.spider.WenxunSpiderSourceConfigDO;
import cn.iocoder.yudao.module.system.service.WenXunSpiderConfigService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;


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
    @Resource
    private WenXunSpiderConfigService wenXunSpiderConfigService;


    @Override
    public Long createDetailCheckInfo(DetailCheckInfoSaveReqVO createReqVO) {
        try {
            // 插入
            DetailCheckInfoDO detailCheckInfo = BeanUtils.toBean(createReqVO, DetailCheckInfoDO.class);
            DetailCheckInfoDO detailCheckInfo2 = detailCheckInfoMapper.selectOne(DetailCheckInfoDO::getSourceUrl, detailCheckInfo.getSourceUrl());
            detailCheckInfo.setUpdateTime(LocalDateTime.now());
            detailCheckInfo.setCreateTime(LocalDateTime.now());
            detailCheckInfo.setCreator("SYSTEM");
            detailCheckInfo.setUpdater("SYSTEM");
            if (detailCheckInfo2 != null) {
                detailCheckInfo.setId(detailCheckInfo2.getId());
                detailCheckInfo.setCreateTime(detailCheckInfo2.getCreateTime());
                detailCheckInfo.setCreator("SYSTEM");
                detailCheckInfo.setUpdater("SYSTEM");

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
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.DETAIL_CHECK_INFO_NOT_EXISTS);

        }
    }

    @Override
    public DetailCheckInfoWithDictDataRespVO getDetailCheckInfo(Long id) {
        DetailCheckInfoDO result = detailCheckInfoMapper.selectById(id);
        if (result != null) {
            WenxunSpiderSourceConfigDO wenxunSpiderSourceConfigDO = wenXunSpiderConfigService.getDataSourceConfig(Long.valueOf(result.getSpiderConfigId()));
            DetailCheckInfoWithDictDataRespVO vo = BeanUtils.toBean(result, DetailCheckInfoWithDictDataRespVO.class);
            vo.setSpiderName(wenxunSpiderSourceConfigDO.getSpiderName());
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