package cn.iocoder.yudao.module.system.service.urlpinglog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.urlpinglog.vo.UrlPingLogPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlpinglog.vo.UrlPingLogSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlpinglog.UrlPingLogDO;

import jakarta.validation.Valid;

/**
 * 网页连通记录-日志 Service 接口
 *
 * @author 文巡智检
 */
public interface UrlPingLogService {

    /**
     * 创建网页连通记录-日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createUrlPingLog(@Valid UrlPingLogSaveReqVO createReqVO);

    /**
     * 更新网页连通记录-日志
     *
     * @param updateReqVO 更新信息
     */
    void updateUrlPingLog(@Valid UrlPingLogSaveReqVO updateReqVO);

    /**
     * 删除网页连通记录-日志
     *
     * @param id 编号
     */
    void deleteUrlPingLog(Integer id);

    /**
     * 获得网页连通记录-日志
     *
     * @param id 编号
     * @return 网页连通记录-日志
     */
    UrlPingLogDO getUrlPingLog(Integer id);

    /**
     * 获得网页连通记录-日志分页
     *
     * @param pageReqVO 分页查询
     * @return 网页连通记录-日志分页
     */
    PageResult<UrlPingLogDO> getUrlPingLogPage(UrlPingLogPageReqVO pageReqVO);

}