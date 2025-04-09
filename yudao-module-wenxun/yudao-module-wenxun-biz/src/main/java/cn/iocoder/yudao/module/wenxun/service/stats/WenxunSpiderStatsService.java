package cn.iocoder.yudao.module.wenxun.service.stats;

import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.*;
import java.util.List;

/**
 * 文巡爬虫统计 Service 接口
 */
public interface WenxunSpiderStatsService {

    /**
     * 获取爬虫数据量统计
     *
     * @return 统计结果
     */
    SpiderCountRespVO getSpiderCount();

    /**
     * 获取爬虫采集成功率统计
     *
     * @return 统计结果
     */
    SpiderSuccessRateRespVO getSpiderSuccessRate();

    /**
     * 获取爬虫采集趋势统计
     *
     * @return 统计结果
     */
    List<SpiderTrendRespVO> getSpiderTrend();

    /**
     * 获取爬虫来源分布统计
     *
     * @return 统计结果
     */
    List<SpiderSourceRespVO> getSpiderSource();

    /**
     * 获取爬虫数据汇总统计
     *
     * @return 统计结果
     */
    SpiderStatsRespVO getSpiderStatsSummary();

    /**
     * 获取爬虫数据多维度统计
     *
     * @param reqVO 请求参数
     * @return 统计结果
     */
    SpiderStatsRespVO getSpiderStatsByDimension(StatsBaseReqVO reqVO);
} 