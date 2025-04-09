package cn.wenxun.admin.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.dal.mysql.wenxunDict.WenXunDictDataMapper;
import cn.iocoder.yudao.module.system.mapper.WenXunSpiderCrawlMapper;
import cn.iocoder.yudao.module.system.model.MeiliSearchInfo;
import cn.wenxun.admin.core.service.MeiliSearchService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 连通性检查
 */
@Slf4j
@Service
public class TestJob implements JobHandler {


    @Resource
    public MeiliSearchService meiliSearchService;
    @Resource
    public WenXunDictDataMapper wenXunDictDataMapper;

    @Resource
    public WenXunSpiderCrawlMapper wenXunSpiderCrawlMapper;

    /**
     * 执行采集定时任务
     *
     * @param param 参数
     * @return
     * @throws Exception
     */
    @Override
    @TenantIgnore

    public String execute(String param) throws Exception {

        // 创建表
//        TaskInfo taskInfo = meiliSearchService.createIndex("wenxun_dict_data", "id");
//        System.out.println("创建表结果: " + JSON.toJSONString(taskInfo));

        // 插入数据
//        List<WenxunSpiderCrawlDetail> details = wenXunSpiderCrawlMapper.selectList();
//        TaskInfo  r= meiliSearchService.add(details);
//        System.out.println("插入数据结果: " +JSON.toJSONString(r));
//        System.out.println("插入数据结果: " + JSON.toJSONString(meiliSearchService.getTask(9)));
//        // 查询数据
//        SearchResult selectResultList = meiliSearchService.search("我校");
//        System.out.println("查询数据结果: " + JSON.toJSONString(selectResultList));
        MeiliSearchInfo searchInfo = new MeiliSearchInfo();
        searchInfo.setPageSize(10);
        searchInfo.setPageNo(1);
        searchInfo.setSpiderId("1");
        searchInfo.setKeyWord("我校");
        meiliSearchService.search(searchInfo);
//        Searchable searchResult = meiliSearchDictService.search("我今天想啊试试敏感词的匹配比如年记使命");
//        SearchRequest searchRequest1 = SearchRequest.builder().q("我想测测年纪念能不能匹配").showRankingScore(true).rankingScoreThreshold(1.0).build();
//
//        Searchable searchResult2 = meiliSearchDictService.search(searchRequest1);
//        System.out.println("查询数据结果: " + JSON.toJSONString(searchResult));

        return null;
    }

}
