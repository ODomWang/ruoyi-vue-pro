package cn.iocoder.yudao.module.system.service.urlpinglog;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.urlpinglog.vo.UrlPingLogPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlpinglog.vo.UrlPingLogSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlpinglog.UrlPingLogDO;
import cn.iocoder.yudao.module.system.dal.mysql.urlpinglog.UrlPingLogMapper;
import cn.iocoder.yudao.module.system.enums.GlobalErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;



/**
 * 网页连通记录-日志 Service 实现类
 *
 * @author 文巡智检
 */
@Service
@Validated
public class UrlPingLogServiceImpl implements UrlPingLogService {

    @Resource
    private UrlPingLogMapper urlPingLogMapper;

    @Override
    public Integer createUrlPingLog(UrlPingLogSaveReqVO createReqVO) {
        // 插入
        UrlPingLogDO urlPingLog = BeanUtils.toBean(createReqVO, UrlPingLogDO.class);
        urlPingLog.setCreateTime(LocalDateTime.now());
        urlPingLog.setUpdateTime(LocalDateTime.now());
        urlPingLogMapper.insert(urlPingLog);
        // 返回
        return urlPingLog.getId();
    }

    @Override
    public void updateUrlPingLog(UrlPingLogSaveReqVO updateReqVO) {
        // 校验存在
        validateUrlPingLogExists(updateReqVO.getId());
        // 更新
        UrlPingLogDO updateObj = BeanUtils.toBean(updateReqVO, UrlPingLogDO.class);
        urlPingLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteUrlPingLog(Integer id) {
        // 校验存在
        validateUrlPingLogExists(id);
        // 删除
        urlPingLogMapper.deleteById(id);
    }

    private void validateUrlPingLogExists(Integer id) {
        if (urlPingLogMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(GlobalErrorCodeConstants.URL_PING_LOG_NOT_EXISTS);
        }
    }

    @Override
    public UrlPingLogDO getUrlPingLog(Integer id) {
        return urlPingLogMapper.selectById(id);
    }

    @Override
    public PageResult<UrlPingLogDO> getUrlPingLogPage(UrlPingLogPageReqVO pageReqVO) {
        return urlPingLogMapper.selectPage(pageReqVO);
    }

}