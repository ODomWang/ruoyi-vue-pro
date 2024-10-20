package cn.wenxun.admin.service;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.wenxun.admin.model.NewsInfo;
import cn.wenxun.admin.model.spider.WenxunSpiderCrawlDetail;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;

import javax.validation.Valid;
import java.util.List;

public interface WenXunSpiderCrawlService {

    /**
     * 创建数据源配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDetail(@Valid List<WenxunSpiderCrawlDetail> createReqVO);

    /**
     * 更新数据源配置
     *
     * @param updateReqVO 更新信息
     */
    void updateDetail(@Valid WenxunSpiderSourceConfigDO updateReqVO);

    /**
     * 删除数据源配置
     *
     * @param id 编号
     */
    void deleteDetail(String id);

    /**
     * 获得数据源配置
     *
     * @param id 编号
     * @return 数据源配置
     */
    WenxunSpiderSourceConfigDO getDetail(Long id);

    /**
     * 获得数据源配置列表
     *
     * @return 数据源配置列表
     */
    PageResult<WenxunSpiderSourceConfigDO> getDetailList(PageParam pageReqVO);

    List<WenxunSpiderSourceConfigDO> getDetailInfo();


    /**
     * 插入数据库通过newinfo
     * newsinfo 转为数据库对象
     */

    void insertDoBySpider(List<NewsInfo> list);


}
