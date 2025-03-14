package cn.wenxun.admin.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.dal.dataobject.urlchangeinfo.UrlChangeInfoDO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlchangelog.UrlChangeLogDO;
import cn.iocoder.yudao.module.system.dal.mysql.urlchangeinfo.UrlChangeInfoMapper;
import cn.iocoder.yudao.module.system.dal.mysql.urlchangelog.UrlChangeLogMapper;
import cn.iocoder.yudao.module.system.mapper.WenXunSpiderCrawlMapper;
import cn.iocoder.yudao.module.system.model.spider.WenxunSpiderCrawlDetail;
import cn.iocoder.yudao.module.system.model.spider.WenxunSpiderSourceConfigDO;
import cn.iocoder.yudao.module.system.service.WenXunSpiderConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static cn.wenxun.admin.job.config.JobConfiguration.NOTIFY_THREAD_POOL_TASK_EXECUTOR;

/**
 * 连通性检查
 */
@Slf4j
@Service
public class UrlChangeJob implements JobHandler {

    @Resource(name = NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private WenXunSpiderConfigService wenXunSpiderConfigService;

    @Resource
    private UrlChangeInfoMapper urlChangeInfoMapper;

    @Resource
    private UrlChangeLogMapper urlChangeLogMapper;

    @Resource
    private WenXunSpiderCrawlMapper wenXunSpiderCrawlMapper;


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

        log.info("执行更新检测定时任务");
        // 线程池采集任务
        List<WenxunSpiderSourceConfigDO> list = wenXunSpiderConfigService.getAllUrlConfigInfo();
        crawlUrlsAsync(list);
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void crawlUrlsAsync(List<WenxunSpiderSourceConfigDO> urls) {
        for (WenxunSpiderSourceConfigDO url : urls) {
            // 数据采集
            CompletableFuture.supplyAsync(() -> checkChange(url), threadPoolTaskExecutor)
                    .thenAccept(data -> {
                        UrlChangeInfoDO urlChangeInfoDO = urlChangeInfoMapper.selectById(data.getSpiderId());
                        if (urlChangeInfoDO == null) {
                            urlChangeInfoDO = new UrlChangeInfoDO();
                            urlChangeInfoDO.setId(data.getSpiderId());
                            urlChangeInfoDO.setUrlName(data.getUrlName());
                            urlChangeInfoDO.setUrl(data.getUrl());
                            urlChangeInfoDO.setAllCount(0);
                            urlChangeInfoDO.setSuccessCount(0);
                            urlChangeInfoDO.setFailCount(0);
                            urlChangeInfoDO.setCreateTime(LocalDateTime.now());
                            urlChangeInfoDO.setUpdateTime(LocalDateTime.now());
                        }
                        urlChangeInfoDO.setDeptId(url.getDeptId());
                        urlChangeInfoDO.setAllCount(urlChangeInfoDO.getAllCount() + 1);
                        urlChangeInfoDO.setLastTitle(data.getTitle());
                        urlChangeInfoDO.setUpdateTime(LocalDateTime.now());
                        if (data.getStatus() == 1) {
                            urlChangeInfoDO.setSuccessCount(urlChangeInfoDO.getSuccessCount() + 1);
                        } else {
                            urlChangeInfoDO.setFailCount(urlChangeInfoDO.getFailCount() + 1);
                        }

                        urlChangeLogMapper.insert(data);
                        urlChangeInfoMapper.insertOrUpdate(urlChangeInfoDO);
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        log.error("连接验证失败: {}", ex.getMessage(), ex);
                        return null;
                    });
            ;
        }
    }

    public UrlChangeLogDO checkChange(WenxunSpiderSourceConfigDO configDO) {
        UrlChangeLogDO urlChangeLogDO = new UrlChangeLogDO();
        urlChangeLogDO.setStatus(0);
        urlChangeLogDO.setUrlName(configDO.getSpiderName());
        urlChangeLogDO.setSpiderId(Math.toIntExact(configDO.getId()));
        urlChangeLogDO.setUrl(configDO.getSpiderUrl());
        urlChangeLogDO.setUpdater("SYSTEM");
        //从表wenxun_spider_crawl_detail查询 获取最后一次采集的数据
        WenxunSpiderCrawlDetail crawlDetail = wenXunSpiderCrawlMapper.selectByCreateTime(urlChangeLogDO.getSpiderId());
        urlChangeLogDO.setCreateTime(LocalDateTime.now());
        urlChangeLogDO.setUpdateTime(LocalDateTime.now());
        if (isToday(crawlDetail.getCreateTime())) {
            urlChangeLogDO.setTitle(crawlDetail.getTitle());
            urlChangeLogDO.setStatus(1);
        }

        return urlChangeLogDO;
    }

    public static boolean isToday(Timestamp timestamp) {
        // 获取当前日期
        LocalDate today = LocalDate.now();

        // 将 Timestamp 转换为 LocalDate
        LocalDate timestampDate = timestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // 比较日期是否相等
        return today.equals(timestampDate);
    }

}
