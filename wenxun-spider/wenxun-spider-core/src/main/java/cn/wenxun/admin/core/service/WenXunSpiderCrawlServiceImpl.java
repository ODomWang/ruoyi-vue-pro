package cn.wenxun.admin.core.service;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wenxun.mapper.WenXunSpiderCrawlMapper;
import cn.iocoder.yudao.module.wenxun.model.NewsInfo;
import cn.iocoder.yudao.module.wenxun.model.spider.WenxunSpiderCrawlDetail;
import cn.iocoder.yudao.module.wenxun.model.spider.WenxunSpiderSourceConfigDO;
import cn.wenxun.admin.job.utils.PlayWrightUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            crawlDetail.setSpiderName(newsInfo.getSpiderName());
            wenxunSpiderCrawlDetailList.add(crawlDetail);
        }
        wenXunSpiderCrawlMapper.insertOrUpdateBatch(wenxunSpiderCrawlDetailList);
        wenxunSpiderCrawlDetailList.forEach(x ->
        {
            x.setContent(PlayWrightUtils.convert(x.getContent()));
            Map<String, String> map = new HashMap<>();
            map.put("name", x.getSpiderName());
            map.put("url", x.getSpiderUrl());
            x.setSpiderUrl(JSON.toJSONString(map));
        });
        meiliSearchService.add(wenxunSpiderCrawlDetailList);


    }
}
