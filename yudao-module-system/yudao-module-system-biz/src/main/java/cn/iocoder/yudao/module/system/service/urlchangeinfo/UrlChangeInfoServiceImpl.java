package cn.iocoder.yudao.module.system.service.urlchangeinfo;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.urlchangeinfo.vo.UrlChangeInfoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlchangeinfo.vo.UrlChangeInfoSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlchangeinfo.UrlChangeInfoDO;
import cn.iocoder.yudao.module.system.dal.mysql.urlchangeinfo.UrlChangeInfoMapper;
import cn.iocoder.yudao.module.system.enums.GlobalErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 网站更新检查 Service 实现类
 *
 * @author 文巡一哥
 */
@Service
@Validated
public class UrlChangeInfoServiceImpl implements UrlChangeInfoService {

    @Resource
    private UrlChangeInfoMapper urlChangeInfoMapper;

    @Override
    public Integer createUrlChangeInfo(UrlChangeInfoSaveReqVO createReqVO) {
        // 插入
        UrlChangeInfoDO urlChangeInfo = BeanUtils.toBean(createReqVO, UrlChangeInfoDO.class);
        urlChangeInfoMapper.insert(urlChangeInfo);
        // 返回
        return urlChangeInfo.getId();
    }

    @Override
    public void updateUrlChangeInfo(UrlChangeInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateUrlChangeInfoExists(updateReqVO.getId());
        // 更新
        UrlChangeInfoDO updateObj = BeanUtils.toBean(updateReqVO, UrlChangeInfoDO.class);
        urlChangeInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteUrlChangeInfo(Integer id) {
        // 校验存在
        validateUrlChangeInfoExists(id);
        // 删除
        urlChangeInfoMapper.deleteById(id);
    }

    private void validateUrlChangeInfoExists(Integer id) {
        if (urlChangeInfoMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.URL_CHANGE_INFO_NOT_EXISTS);
        }
    }

    @Override
    public UrlChangeInfoDO getUrlChangeInfo(Integer id) {
        return urlChangeInfoMapper.selectById(id);
    }

    @Override
    public PageResult<UrlChangeInfoDO> getUrlChangeInfoPage(UrlChangeInfoPageReqVO pageReqVO) {
        return urlChangeInfoMapper.selectPage(pageReqVO);
    }

}