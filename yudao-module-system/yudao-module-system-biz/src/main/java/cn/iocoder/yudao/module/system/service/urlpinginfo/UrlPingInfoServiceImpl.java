package cn.iocoder.yudao.module.system.service.urlpinginfo;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo.UrlPingInfoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo.UrlPingInfoSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlpinginfo.UrlPingInfoDO;
import cn.iocoder.yudao.module.system.dal.mysql.urlpinginfo.UrlPingInfoMapper;
import cn.iocoder.yudao.module.system.enums.GlobalErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;



/**
 * 网页连通记录 Service 实现类
 *
 * @author 文巡智检
 */
@Service
@Validated
public class UrlPingInfoServiceImpl implements UrlPingInfoService {

    @Resource
    private UrlPingInfoMapper urlPingInfoMapper;

    @Override
    public Integer createUrlPingInfo(UrlPingInfoSaveReqVO createReqVO) {
        // 插入
        UrlPingInfoDO urlPingInfo = BeanUtils.toBean(createReqVO, UrlPingInfoDO.class);
        urlPingInfoMapper.insert(urlPingInfo);
        // 返回
        return urlPingInfo.getId();
    }

    @Override
    public void updateUrlPingInfo(UrlPingInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateUrlPingInfoExists(updateReqVO.getId());
        // 更新
        UrlPingInfoDO updateObj = BeanUtils.toBean(updateReqVO, UrlPingInfoDO.class);
        urlPingInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteUrlPingInfo(Integer id) {
        // 校验存在
        validateUrlPingInfoExists(id);
        // 删除
        urlPingInfoMapper.deleteById(id);
    }

    private void validateUrlPingInfoExists(Integer id) {
        if (urlPingInfoMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.URL_PING_INFO_NOT_EXISTS);
        }
    }

    @Override
    public UrlPingInfoDO getUrlPingInfo(Integer id) {
        return urlPingInfoMapper.selectById(id);
    }

    @Override
    public PageResult<UrlPingInfoDO> getUrlPingInfoPage(UrlPingInfoPageReqVO pageReqVO) {
        return urlPingInfoMapper.selectPage(pageReqVO);
    }

}