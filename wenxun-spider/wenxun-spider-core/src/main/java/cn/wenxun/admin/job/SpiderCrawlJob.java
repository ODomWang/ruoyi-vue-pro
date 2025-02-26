package cn.wenxun.admin.job;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataService;
import cn.iocoder.yudao.module.wenxun.controller.admin.auditlog.vo.AuditLogSaveReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.DetailCheckInfoSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import cn.iocoder.yudao.module.wenxun.enums.commondao.WrongWordInfo;
import cn.iocoder.yudao.module.wenxun.model.NewsInfo;
import cn.iocoder.yudao.module.wenxun.model.spider.SpiderXpathConfigDO;
import cn.iocoder.yudao.module.wenxun.model.spider.WenxunSpiderSourceConfigDO;
import cn.iocoder.yudao.module.wenxun.service.WenXunSpiderConfigService;
import cn.iocoder.yudao.module.wenxun.service.auditlog.AuditLogService;
import cn.iocoder.yudao.module.wenxun.service.detailcheckinfo.DetailCheckInfoService;
import cn.wenxun.admin.core.service.WenXunSpiderCrawlService;
import cn.wenxun.admin.job.utils.PlayWrightUtils;
import cn.wenxun.admin.job.utils.SensitiveFilter;
import cn.wenxun.admin.job.utils.trie.TextCorrection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    @Resource
    private AuditLogService auditLogService;


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
            SpiderXpathConfigDO createReqVO = BeanUtil.copyProperties(url, SpiderXpathConfigDO.class);
            log.info("----开始进行：【" + createReqVO.getSpiderName() + "】 采集任务----");

            /* 数据采集        List<NewsInfo> newsInfos = PlayWrightUtils.crawlUrl(createReqVO); */
            CompletableFuture.supplyAsync(() -> PlayWrightUtils.crawlUrl(createReqVO, wenXunSpiderCrawlService), threadPoolTaskExecutor)
                    .thenAccept(data -> {
                        log.info("采集任务执行完毕，数据开始入库...");
                        // 结果保存
                        if (data != null) {
//                            wenXunSpiderCrawlService.insertDoBySpider(data);
                            log.info("数据入库完毕。");
                            log.info("开始进行敏感词检测...");
                            checkSensitiveWords(data);
                            log.info("敏感词检测完毕。");
                            //进入人工研判表
                            log.info("同步数据到审核表。");
                            syncCheckToAudit();
                            log.info("---- 【" + createReqVO.getSpiderName() + "】 采集任务处理【结束】----");

                        }
                    }).exceptionally(ex -> {
                        ex.printStackTrace();
                        log.error("采集任务执行异常：{}", ex.getMessage());
                        return null;
                    });
        }
    }

    /**
     * 敏感词检测
     */
    public void checkSensitiveWords(List<NewsInfo> data) {
        for (NewsInfo info : data) {
            Set<String> s2 = SensitiveFilter.getMatchingWords(info.getContent());

            //查询所有词库信息
            List<WenXunDictDataDO> list = wenXunDictDataService.getDictDataListByDatas(s2);
            // 对词库信息进行有效判断
            if (list != null && !list.isEmpty()) {
                Set<String> correctWords = list.stream().filter(wenXunDictDataDO -> wenXunDictDataDO.getDictType().equals("wrong_word_configuration"))
                        .map(WenXunDictDataDO::getValue).collect(Collectors.toSet());
                String mk = info.getContent();
                // 对错误做初步过滤
                s2 = TextCorrection.filterWrongWords(mk, s2, correctWords);

                List<WrongWordInfo> ids = new java.util.ArrayList<>();
                // 敏感词入库
                for (WenXunDictDataDO wenXunDictDataDO : list) {
                    //敏感词
                    if (wenXunDictDataDO.getDictType().equals("wrong_word_configuration") && s2.contains(wenXunDictDataDO.getLabel())) {
                        boolean b = false;
                        //不包含冲突词
                        if (StringUtils.isNotEmpty(wenXunDictDataDO.getRemark())) {
                            String[] split = wenXunDictDataDO.getRemark().split("\\|");
                            for (String s : split) {
                                if (info.getContent().contains(s)) {
                                    b = true;
                                }
                            }
                        }
                        if (!b) {
                            ids.add(WrongWordInfo.builder()
                                    .wrongWord(wenXunDictDataDO.getLabel()).
                                    rightWord(wenXunDictDataDO.getValue()).remark(wenXunDictDataDO.getRemark())
                                    .colorType(wenXunDictDataDO.getColorType())
                                    .wrongWordType(wenXunDictDataDO.getDictType())
                                    .build());
                            mk = highlightText(mk, wenXunDictDataDO.getLabel());
                        }
                        //落马官员
                    } else if (wenXunDictDataDO.getDictType().equals("allen_official")) {

                        if (s2.contains(wenXunDictDataDO.getValue()) && s2.contains(wenXunDictDataDO.getRemark())) {
                            mk = highlightText(mk, wenXunDictDataDO.getLabel(), wenXunDictDataDO.getRemark(), wenXunDictDataDO.getValue());
                            ids.add(WrongWordInfo.builder()
                                    .wrongWord(wenXunDictDataDO.getLabel()).
                                    rightWord(wenXunDictDataDO.getValue()).remark(wenXunDictDataDO.getRemark())
                                    .colorType(wenXunDictDataDO.getColorType())
                                    .wrongWordType(wenXunDictDataDO.getDictType())
                                    .build());

                        }
                    }

                }
                if (!CollectionUtils.isEmpty(ids)) {
                    DetailCheckInfoSaveReqVO reqVO = new DetailCheckInfoSaveReqVO();
                    reqVO.setCheckDetail(mk);
                    reqVO.setTargetDetail(JSON.toJSONString(ids));
                    reqVO.setStatus(0);
                    reqVO.setCheckSource((int) info.getConfigId());
                    reqVO.setSourceUrl(info.getUrl());
                    reqVO.setSpiderConfigId(info.getConfigId());
                    reqVO.setWebIcon(info.getWebIcon());
                    reqVO.setTitleDesc(info.getDesc());
                    detailCheckInfoService.createDetailCheckInfo(reqVO);
                }

            }
        }
    }

    public static JSONObject checkSensitiveWords(String content, WenXunDictDataService wenXunDictDataService) {

        Set<String> s2 = SensitiveFilter.getMatchingWords(content);

        //查询所有词库信息
        List<WenXunDictDataDO> list = wenXunDictDataService.getDictDataListByDatas(s2);
        // 对词库信息进行有效判断
        if (list != null && !list.isEmpty()) {
            Set<String> correctWords = list.stream().filter(wenXunDictDataDO -> wenXunDictDataDO.getDictType().equals("wrong_word_configuration"))
                    .map(WenXunDictDataDO::getValue).collect(Collectors.toSet());
            String mk = content;
            // 对错误做初步过滤
            s2 = TextCorrection.filterWrongWords(mk, s2, correctWords);

            List<WrongWordInfo> ids = new java.util.ArrayList<>();
            // 敏感词入库
            for (WenXunDictDataDO wenXunDictDataDO : list) {
                //敏感词
                if (wenXunDictDataDO.getDictType().equals("wrong_word_configuration") && s2.contains(wenXunDictDataDO.getLabel())) {
                    boolean b = false;
                    //不包含冲突词
                    if (StringUtils.isNotEmpty(wenXunDictDataDO.getRemark())) {
                        String[] split = wenXunDictDataDO.getRemark().split("\\|");
                        for (String s : split) {
                            if (content.contains(s)) {
                                b = true;
                            }
                        }
                    }
                    if (!b) {
                        ids.add(WrongWordInfo.builder()
                                .wrongWord(wenXunDictDataDO.getLabel()).
                                rightWord(wenXunDictDataDO.getValue()).remark(wenXunDictDataDO.getRemark())
                                .colorType(wenXunDictDataDO.getColorType())
                                .wrongWordType(wenXunDictDataDO.getDictType())
                                .build());
                        mk = highlightText(mk, wenXunDictDataDO.getLabel());
                    }
                    //落马官员
                } else if (wenXunDictDataDO.getDictType().equals("allen_official")) {

                    if (s2.contains(wenXunDictDataDO.getValue()) && s2.contains(wenXunDictDataDO.getRemark())) {
                        mk = highlightText(mk, wenXunDictDataDO.getLabel(), wenXunDictDataDO.getRemark(), wenXunDictDataDO.getValue());
                        ids.add(WrongWordInfo.builder()
                                .wrongWord(wenXunDictDataDO.getLabel()).
                                rightWord(wenXunDictDataDO.getValue()).remark(wenXunDictDataDO.getRemark())
                                .colorType(wenXunDictDataDO.getColorType())
                                .wrongWordType(wenXunDictDataDO.getDictType())
                                .build());

                    }
                }

            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content", mk);
            jsonObject.put("wrongWordInfoList", JSON.toJSONString(ids));

            return jsonObject;

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", content);
        jsonObject.put("wrongWordInfoList", JSON.toJSONString(new ArrayList<>()));

        return jsonObject;
    }


    public static String highlightText(String text, String... targetPhrases) {
        for (String targetPhrase : targetPhrases) {
            // 将目标词组加粗并标红，使用 HTML 的方式来实现
            String replacement = "<span style=\"color: red; font-weight: bold; background-color: #ffe4e1; padding: 2px;\">"
                    + targetPhrase + "</span>";
            // 使用正则表达式进行全局替换
            text = text.replaceAll("(?i)" + targetPhrase, replacement);
        }
        return text;
    }


    /**
     * 同步数据到审核表
     */

    public void syncCheckToAudit() {
        // 使用 JOIN 查询校验表和审核表，筛选出审核表中不存在的记录
        List<DetailCheckInfoDO> unsyncedCheckInfoList = detailCheckInfoService.selectJoinList();

        // 遍历未同步的校验表数据并插入审核表
        for (DetailCheckInfoDO checkInfo : unsyncedCheckInfoList) {
            AuditLogSaveReqVO newAuditLog = new AuditLogSaveReqVO();
            newAuditLog.setSpiderId(checkInfo.getId().toString());
            newAuditLog.setApprovedRecord(null); // 占位，必要时填充
            newAuditLog.setRejectedRecord(null); // 占位，必要时填充
            newAuditLog.setStatus(0); // 默认状态
            newAuditLog.setUpdater("system"); // 默认操作员
            auditLogService.createAuditLog(newAuditLog);
        }
    }
}
