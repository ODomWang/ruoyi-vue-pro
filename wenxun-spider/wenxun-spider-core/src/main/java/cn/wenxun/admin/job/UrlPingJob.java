package cn.wenxun.admin.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.wenxun.controller.admin.urlpinglog.vo.UrlPingLogSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlpinginfo.UrlPingInfoDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.urlpinginfo.UrlPingInfoMapper;
import cn.iocoder.yudao.module.wenxun.service.urlpinginfo.UrlPingInfoService;
import cn.iocoder.yudao.module.wenxun.service.urlpinglog.UrlPingLogService;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import cn.wenxun.admin.service.WenXunSpiderConfigService;
import cn.wenxun.admin.service.WenXunSpiderCrawlService;
import lombok.extern.slf4j.Slf4j;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.html.HtmlPage;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static cn.wenxun.admin.job.config.JobConfiguration.NOTIFY_THREAD_POOL_TASK_EXECUTOR;

/**
 * 连通性检查
 */
@Slf4j
@Service
public class UrlPingJob implements JobHandler {

    @Resource(name = NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private WenXunSpiderConfigService wenXunSpiderConfigService;

    @Resource
    private UrlPingInfoMapper urlPingInfoMapper;
    @Resource
    private UrlPingLogService urlPingLogService;


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

        log.info("执行连通性检测定时任务");
        // 线程池采集任务
        List<WenxunSpiderSourceConfigDO> list = wenXunSpiderConfigService.getAllUrlConfigInfo();
        crawlUrlsAsync(list);
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    public void crawlUrlsAsync(List<WenxunSpiderSourceConfigDO> urls) {
        for (WenxunSpiderSourceConfigDO url : urls) {
            // 数据采集
            CompletableFuture.supplyAsync(() -> isURLAccessible(url), threadPoolTaskExecutor)
                    .thenAccept(data -> {
                        log.info("连接验证完毕");
                        // 结果保存
                        if (data != null) {
                            urlPingLogService.createUrlPingLog(data);
                            UrlPingInfoDO urlPingInfoDO = urlPingInfoMapper.selectById(data.getPingId());
                            if (urlPingInfoDO == null) {
                                urlPingInfoDO = new UrlPingInfoDO();
                                urlPingInfoDO.setId(data.getPingId());
                                urlPingInfoDO.setAllCount(1);
                                urlPingInfoDO.setCreateTime(LocalDateTime.now());
                                urlPingInfoDO.setUrlName(data.getUrlName());

                                if (data.getStatus() == 1) {
                                    urlPingInfoDO.setSuccessCount(1);
                                    urlPingInfoDO.setFailCount(0);
                                } else {
                                    urlPingInfoDO.setSuccessCount(0);
                                    urlPingInfoDO.setFailCount(1);
                                }
                            } else {
                                urlPingInfoDO.setUrlName(data.getUrlName());
                                urlPingInfoDO.setAllCount(urlPingInfoDO.getAllCount() + 1);
                                if (data.getStatus() == 1) {
                                    urlPingInfoDO.setSuccessCount(urlPingInfoDO.getSuccessCount() + 1);
                                } else {
                                    urlPingInfoDO.setFailCount(urlPingInfoDO.getFailCount() + 1);
                                }
                            }
                            urlPingInfoDO.setUrl(data.getUrl());
                            urlPingInfoDO.setUpdateTime(LocalDateTime.now());
                            urlPingInfoMapper.insertOrUpdate(urlPingInfoDO);
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        log.error("连接验证失败: {}", ex.getMessage(), ex);
                        return null;
                    });;
        }
    }

    public static UrlPingLogSaveReqVO isURLAccessible(WenxunSpiderSourceConfigDO configDO) {
        UrlPingLogSaveReqVO urlPingLogSaveReqVO = new UrlPingLogSaveReqVO();
        urlPingLogSaveReqVO.setPingId(Math.toIntExact(configDO.getId()));
        urlPingLogSaveReqVO.setUrlName(configDO.getSpiderName());
        urlPingLogSaveReqVO.setUrl(configDO.getSpiderUrl());
        urlPingLogSaveReqVO.setUpdater("SYSTEM");
         try (WebClient webClient = new WebClient()) {
            // 禁用 CSS 和 JavaScript 支持以提高性能
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            // 加载 URL
            HtmlPage page = webClient.getPage(configDO.getSpiderUrl());
            WebResponse response = page.getWebResponse();

            // 检查响应状态码
            int statusCode = response.getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                urlPingLogSaveReqVO.setStatus(1);
            } else {
                urlPingLogSaveReqVO.setStatus(0);
            }
            urlPingLogSaveReqVO.setPingCode(statusCode + "");
        } catch (Exception e) {
            // 捕获异常，表示 URL 不可用
            urlPingLogSaveReqVO.setStatus(0);
            urlPingLogSaveReqVO.setPingCode("-1");

        }
        return urlPingLogSaveReqVO;
    }


}
