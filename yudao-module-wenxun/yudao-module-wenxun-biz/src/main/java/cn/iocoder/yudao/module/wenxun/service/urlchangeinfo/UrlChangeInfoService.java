package cn.iocoder.yudao.module.wenxun.service.urlchangeinfo;

import cn.iocoder.yudao.module.wenxun.controller.admin.urlchangeinfo.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlchangeinfo.UrlChangeInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.validation.Valid;

/**
 * 网站更新检查 Service 接口
 *
 * @author 文巡一哥
 */
public interface UrlChangeInfoService {

    /**
     * 创建网站更新检查
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createUrlChangeInfo(@Valid UrlChangeInfoSaveReqVO createReqVO);

    /**
     * 更新网站更新检查
     *
     * @param updateReqVO 更新信息
     */
    void updateUrlChangeInfo(@Valid UrlChangeInfoSaveReqVO updateReqVO);

    /**
     * 删除网站更新检查
     *
     * @param id 编号
     */
    void deleteUrlChangeInfo(Integer id);

    /**
     * 获得网站更新检查
     *
     * @param id 编号
     * @return 网站更新检查
     */
    UrlChangeInfoDO getUrlChangeInfo(Integer id);

    /**
     * 获得网站更新检查分页
     *
     * @param pageReqVO 分页查询
     * @return 网站更新检查分页
     */
    PageResult<UrlChangeInfoDO> getUrlChangeInfoPage(UrlChangeInfoPageReqVO pageReqVO);

}