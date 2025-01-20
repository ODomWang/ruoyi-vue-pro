package cn.wenxun.admin.service;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.wenxun.admin.model.spider.SpiderXpathConfigDO;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;

import javax.validation.Valid;
import java.util.List;

public interface WenXunSpiderConfigService {

    /**
     * 创建数据源配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDataSourceConfig(@Valid SpiderXpathConfigDO createReqVO);

    /**
     * 更新数据源配置
     *
     * @param updateReqVO 更新信息
     */
    void updateDataSourceConfig(@Valid WenxunSpiderSourceConfigDO updateReqVO);

    /**
     * 删除数据源配置
     *
     * @param id 编号
     */
    void deleteDataSourceConfig(String id);

    /**
     * 获得数据源配置
     *
     * @param id 编号
     * @return 数据源配置
     */
    WenxunSpiderSourceConfigDO getDataSourceConfig(Long id);

    /**
     * 获得数据源配置列表
     *
     * @return 数据源配置列表
     */
    PageResult<WenxunSpiderSourceConfigDO> getDataSourceConfigList(PageParam pageReqVO);

    List<WenxunSpiderSourceConfigDO> getAllUrlConfigInfo();


}
