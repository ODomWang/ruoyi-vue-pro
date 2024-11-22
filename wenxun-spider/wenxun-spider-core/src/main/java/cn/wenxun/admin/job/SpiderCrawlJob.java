package cn.wenxun.admin.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataService;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.DetailCheckInfoSaveReqVO;
import cn.iocoder.yudao.module.wenxun.service.detailcheckinfo.DetailCheckInfoService;
import cn.wenxun.admin.job.utils.HtmlUnitUtil;
import cn.wenxun.admin.job.utils.SensitiveFilter;
import cn.wenxun.admin.model.NewsInfo;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import cn.wenxun.admin.service.WenXunSpiderConfigService;
import cn.wenxun.admin.service.WenXunSpiderCrawlService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static cn.wenxun.admin.job.config.JobConfiguration.NOTIFY_THREAD_POOL_TASK_EXECUTOR;

@Slf4j
@Service
public class SpiderCrawlJob implements JobHandler {

    @Resource(name = NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private WenXunSpiderConfigService wenXunSpiderConfigService;
    @Resource
    private WenXunSpiderCrawlService wenXunSpiderCrawlService;
    @Resource
    private WenXunDictDataService wenXunDictDataService;

    @Resource
    private DetailCheckInfoService detailCheckInfoService;

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

        log.info("执行采集定时任务");
        // 线程池采集任务
        List<WenxunSpiderSourceConfigDO> list = wenXunSpiderConfigService.getAllUrlConfigInfo();
        crawlUrlsAsync(list);
        return null;
    }

    public void crawlUrlsAsync(List<WenxunSpiderSourceConfigDO> urls) {
        for (WenxunSpiderSourceConfigDO url : urls) {
             // 数据采集
            CompletableFuture.supplyAsync(() -> HtmlUnitUtil.crawlUrl(url, false), threadPoolTaskExecutor)
                    .thenAccept(data -> {
                        log.info("采集任务执行完毕，数据开始入库...");
                        // 结果保存
                        if (data != null) {
                            wenXunSpiderCrawlService.insertDoBySpider(data);
                            log.info("数据入库完毕。");
                            log.info("开始进行敏感词检测...");
                            checkSensitiveWords(data);
                            log.info("敏感词检测完毕。");

                            //敏感词对比

                        }
                    });
        }
    }

    /**
     * 敏感词检测
     */
    public void checkSensitiveWords(List<NewsInfo> data) {
        for (NewsInfo info : data) {
            Set<String> s2 = SensitiveFilter.getMatchingWords(info.getContent(),wenXunDictDataService);

            //查询所有词库信息
            List<WenXunDictDataDO> list = wenXunDictDataService.getDictDataListByDatas(s2);
            // 对词库信息进行有效判断
            if (list != null && !list.isEmpty()) {
                String mk = info.getContent();
                Set<Long> ids = new java.util.HashSet<>();
                // 敏感词入库
                for (WenXunDictDataDO wenXunDictDataDO : list) {
                    //敏感词
                    if (wenXunDictDataDO.getDictType().equals("wrong_word_configuration")) {
                        boolean b = false;
                        //不包含冲突词
                        if (StringUtils.isNotEmpty(wenXunDictDataDO.getRemark())) {
                            String[] split = wenXunDictDataDO.getRemark().split("|");
                            for (String s : split) {
                                if (info.getContent().contains(s)) {
                                    b = true;
                                }
                            }
                        }
                        if (!b) {
                            ids.add(wenXunDictDataDO.getId());
                            mk = highlightText(mk, wenXunDictDataDO.getLabel());
                        }
                        //落马官员
                    } else if (wenXunDictDataDO.getDictType().equals("allen_official")) {

                        if (s2.contains(wenXunDictDataDO.getValue()) && s2.contains(wenXunDictDataDO.getRemark())) {
                            mk = highlightText(mk, wenXunDictDataDO.getLabel(), wenXunDictDataDO.getRemark(), wenXunDictDataDO.getValue());
                            ids.add(wenXunDictDataDO.getId());

                        }
                    }

                }
                if (!CollectionUtils.isEmpty(ids)) {
                    DetailCheckInfoSaveReqVO reqVO = new DetailCheckInfoSaveReqVO();
                    reqVO.setCheckDetail(String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)));                    reqVO.setTargetDetail(mk);
                    reqVO.setStatus(0);
                    reqVO.setCheckSource(-1);
                    reqVO.setSourceUrl(info.getUrl());
                    reqVO.setSpiderConfigId(info.getConfigId());
                    reqVO.setWebIcon(info.getWebIcon());
                    detailCheckInfoService.createDetailCheckInfo(reqVO);
                }

            }
        }
    }

    public static String highlightText(String text, String... targetPhrases) {
        for (String targetPhrase : targetPhrases) {
            // 将目标词组加粗并标红，使用 HTML 的方式来实现
            String replacement = "**<span style=\"color:red\">" + targetPhrase + "</span>**";
            text = text.replaceAll(targetPhrase, replacement);
        }
        return text;
    }
}
