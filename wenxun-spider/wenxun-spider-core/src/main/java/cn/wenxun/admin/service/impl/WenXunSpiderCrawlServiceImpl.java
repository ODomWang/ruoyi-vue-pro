package cn.wenxun.admin.service.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.wenxun.admin.core.service.MeiliSearchService;
import cn.wenxun.admin.job.utils.PlayWrightUtils;
import cn.wenxun.admin.mapper.WenXunSpiderCrawlMapper;
import cn.wenxun.admin.model.NewsInfo;
import cn.wenxun.admin.model.spider.WenxunSpiderCrawlDetail;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import cn.wenxun.admin.service.WenXunSpiderCrawlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Slf4j
public class WenXunSpiderCrawlServiceImpl implements WenXunSpiderCrawlService {
    @Resource
    private WenXunSpiderCrawlMapper wenXunSpiderCrawlMapper;

    /**
     * @param createReqVO 创建信息
     * @return
     */
    @Override
    public Long createDetail(List<WenxunSpiderCrawlDetail> createReqVO) {
        wenXunSpiderCrawlMapper.insertBatch(createReqVO);
        return null;
    }

    /**
     * @param updateReqVO 更新信息
     */
    @Override
    public void updateDetail(WenxunSpiderSourceConfigDO updateReqVO) {

    }

    /**
     * @param id 编号
     */
    @Override
    public void deleteDetail(String id) {

    }

    /**
     * @param id 编号
     * @return
     */
    @Override
    public WenxunSpiderSourceConfigDO getDetail(Long id) {
        return null;
    }

    /**
     * @param pageReqVO
     * @return
     */
    @Override
    public PageResult<WenxunSpiderSourceConfigDO> getDetailList(PageParam pageReqVO) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<WenxunSpiderSourceConfigDO> getDetailInfo() {
        return null;
    }

    @Resource
    public MeiliSearchService meiliSearchService;

    /**
     *
     */
    @Override
    public void insertDoBySpider(List<NewsInfo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<WenxunSpiderCrawlDetail> wenxunSpiderCrawlDetailList = new ArrayList<>();
        for (NewsInfo newsInfo : list) {
            WenxunSpiderCrawlDetail crawlDetail = wenXunSpiderCrawlMapper.selectByUrl(newsInfo.getUrl());
            if (crawlDetail == null) {
                crawlDetail = new WenxunSpiderCrawlDetail();
            }
            crawlDetail.setDate(newsInfo.getDate());
            crawlDetail.setAuthor(newsInfo.getAuthor());
            crawlDetail.setContent(newsInfo.getContent());
            crawlDetail.setSpiderUrl(newsInfo.getUrl());
            crawlDetail.setTitle(newsInfo.getTitle());
            crawlDetail.setTitleDesc(newsInfo.getDesc());
            crawlDetail.setSpiderConfigId(newsInfo.getConfigId());
            crawlDetail.setDate("1");
            crawlDetail.setIcon(newsInfo.getWebIcon());
            wenxunSpiderCrawlDetailList.add(crawlDetail);
        }
        wenXunSpiderCrawlMapper.insertOrUpdateBatch(wenxunSpiderCrawlDetailList);
//        log.info("数据库插入成功【" + wenxunSpiderCrawlDetailList.size() + "】条");
        wenxunSpiderCrawlDetailList.forEach(x -> x.setContent(PlayWrightUtils.convert(x.getContent())));
        meiliSearchService.add(wenxunSpiderCrawlDetailList);
//        log.info("搜索引擎插入结束,插入结果【" + meiliSearchService.add(wenxunSpiderCrawlDetailList) + "】");


    }
}
