package cn.iocoder.yudao.module.system.service.urlchangelog;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.urlchangelog.vo.UrlChangeLogPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlchangelog.vo.UrlChangeLogSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlchangelog.UrlChangeLogDO;
import cn.iocoder.yudao.module.system.dal.mysql.urlchangelog.UrlChangeLogMapper;
import cn.iocoder.yudao.module.system.enums.GlobalErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;


/**
 * 网站更新检查-日志 Service 实现类
 *
 * @author 文巡一哥
 */
@Service
@Validated
public class UrlChangeLogServiceImpl implements UrlChangeLogService {

    @Resource
    private UrlChangeLogMapper urlChangeLogMapper;

    @Override
    public Integer createUrlChangeLog(UrlChangeLogSaveReqVO createReqVO) {
        // 插入
        UrlChangeLogDO urlChangeLog = BeanUtils.toBean(createReqVO, UrlChangeLogDO.class);
        urlChangeLogMapper.insert(urlChangeLog);
        // 返回
        return urlChangeLog.getId();
    }

    @Override
    public void updateUrlChangeLog(UrlChangeLogSaveReqVO updateReqVO) {
        // 校验存在
        validateUrlChangeLogExists(updateReqVO.getId());
        // 更新
        UrlChangeLogDO updateObj = BeanUtils.toBean(updateReqVO, UrlChangeLogDO.class);
        urlChangeLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteUrlChangeLog(Integer id) {
        // 校验存在
        validateUrlChangeLogExists(id);
        // 删除
        urlChangeLogMapper.deleteById(id);
    }

    private void validateUrlChangeLogExists(Integer id) {
        if (urlChangeLogMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.URL_CHANGE_LOG_NOT_EXISTS);
        }
    }

    @Override
    public UrlChangeLogDO getUrlChangeLog(Integer id) {
        return urlChangeLogMapper.selectById(id);
    }

    @Override
    public PageResult<UrlChangeLogDO> getUrlChangeLogPage(UrlChangeLogPageReqVO pageReqVO) {
        return urlChangeLogMapper.selectPage(pageReqVO);
    }

}