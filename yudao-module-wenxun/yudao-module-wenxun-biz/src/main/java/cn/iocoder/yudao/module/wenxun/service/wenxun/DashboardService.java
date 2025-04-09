package cn.iocoder.yudao.module.wenxun.service.wenxun;

import cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo.*;

/**
 * 数字大屏 Service 接口
 */
public interface DashboardService {

    /**
     * 获取数字大屏概览数据
     *
     * @param timeRange 时间范围
     * @return 概览数据
     */
    DashboardOverviewRespVO getOverview(String timeRange);

    /**
     * 获取网站监测数据
     *
     * @param timeRange 时间范围
     * @param siteId 网站ID
     * @return 网站监测数据
     */
    DashboardSitesMonitoringRespVO getSitesMonitoring(String timeRange, Integer siteId);

    /**
     * 获取错误分析数据
     *
     * @param timeRange 时间范围
     * @param deptId 部门ID
     * @return 错误分析数据
     */
    DashboardErrorAnalysisRespVO getErrorAnalysis(String timeRange, Long deptId);

    /**
     * 获取部门工作统计数据
     *
     * @param timeRange 时间范围
     * @return 部门工作统计数据
     */
    DashboardDepartmentStatsRespVO getDepartmentStats(String timeRange);

    /**
     * 获取文本质量数据
     *
     * @param timeRange 时间范围
     * @param siteId 网站ID
     * @return 文本质量数据
     */
    DashboardTextQualityRespVO getTextQuality(String timeRange, Integer siteId);

    /**
     * 获取实时统计数据
     *
     * @return 实时统计数据
     */
    DashboardRealTimeStatsRespVO getRealTimeStats();

    /**
     * 获取时间选项数据
     *
     * @return 时间选项数据
     */
    DashboardTimeOptionsRespVO getTimeOptions();

    /**
     * 获取筛选选项数据
     *
     * @return 筛选选项数据
     */
    DashboardFilterOptionsRespVO getFilterOptions();
} 