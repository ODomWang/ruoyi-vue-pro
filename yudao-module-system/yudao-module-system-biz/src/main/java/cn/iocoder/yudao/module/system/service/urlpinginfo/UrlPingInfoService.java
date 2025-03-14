package cn.iocoder.yudao.module.system.service.urlpinginfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo.UrlPingInfoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo.UrlPingInfoSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlpinginfo.UrlPingInfoDO;

import jakarta.validation.Valid;

/**
 * 网页连通记录 Service 接口
 *
 * @author 文巡智检
 */
public interface UrlPingInfoService {

    /**
     * 创建网页连通记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createUrlPingInfo(@Valid UrlPingInfoSaveReqVO createReqVO);

    /**
     * 更新网页连通记录
     *
     * @param updateReqVO 更新信息
     */
    void updateUrlPingInfo(@Valid UrlPingInfoSaveReqVO updateReqVO);

    /**
     * 删除网页连通记录
     *
     * @param id 编号
     */
    void deleteUrlPingInfo(Integer id);

    /**
     * 获得网页连通记录
     *
     * @param id 编号
     * @return 网页连通记录
     */
    UrlPingInfoDO getUrlPingInfo(Integer id);

    /**
     * 获得网页连通记录分页
     *
     * @param pageReqVO 分页查询
     * @return 网页连通记录分页
     */
    PageResult<UrlPingInfoDO> getUrlPingInfoPage(UrlPingInfoPageReqVO pageReqVO);

}