package cn.wenxun.admin.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.wenxun.admin.job.utils.HtmlUnitUtil;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import cn.wenxun.admin.service.WenXunSpiderConfigService;
import cn.wenxun.admin.service.WenXunSpiderCrawlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static cn.wenxun.admin.job.config.JobConfiguration.NOTIFY_THREAD_POOL_TASK_EXECUTOR;

@Slf4j
@Component
public class SpiderCrawlJob implements JobHandler {

    @Resource(name = NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private WenXunSpiderConfigService wenXunSpiderConfigService;
    @Resource
    private WenXunSpiderCrawlService wenXunSpiderCrawlService;


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
            CompletableFuture.supplyAsync(() -> HtmlUnitUtil.crawlUrl(url), threadPoolTaskExecutor)
                    .thenAccept(data -> {
                        if (data != null) {
                             wenXunSpiderCrawlService.insertDoBySpider(data);
                        }
                    });
            ;
        }
    }


}
