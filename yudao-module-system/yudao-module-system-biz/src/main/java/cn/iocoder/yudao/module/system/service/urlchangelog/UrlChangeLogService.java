package cn.iocoder.yudao.module.system.service.urlchangelog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.urlchangelog.vo.UrlChangeLogPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlchangelog.vo.UrlChangeLogSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlchangelog.UrlChangeLogDO;

import jakarta.validation.Valid;

/**
 * 网站更新检查-日志 Service 接口
 *
 * @author 文巡一哥
 */
public interface UrlChangeLogService {

    /**
     * 创建网站更新检查-日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createUrlChangeLog(@Valid UrlChangeLogSaveReqVO createReqVO);

    /**
     * 更新网站更新检查-日志
     *
     * @param updateReqVO 更新信息
     */
    void updateUrlChangeLog(@Valid UrlChangeLogSaveReqVO updateReqVO);

    /**
     * 删除网站更新检查-日志
     *
     * @param id 编号
     */
    void deleteUrlChangeLog(Integer id);

    /**
     * 获得网站更新检查-日志
     *
     * @param id 编号
     * @return 网站更新检查-日志
     */
    UrlChangeLogDO getUrlChangeLog(Integer id);

    /**
     * 获得网站更新检查-日志分页
     *
     * @param pageReqVO 分页查询
     * @return 网站更新检查-日志分页
     */
    PageResult<UrlChangeLogDO> getUrlChangeLogPage(UrlChangeLogPageReqVO pageReqVO);

}