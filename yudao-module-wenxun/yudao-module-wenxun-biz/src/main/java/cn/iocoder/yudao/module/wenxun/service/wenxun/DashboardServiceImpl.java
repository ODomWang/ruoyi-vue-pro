package cn.iocoder.yudao.module.wenxun.service.wenxun;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.mysql.DashboardMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数字大屏 Service 实现类
 */
@Service
@Validated
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private DashboardMapper dashboardMapper;

    @Override
    public DashboardOverviewRespVO getOverview(String timeRange) {
        // 解析时间范围
        Map<String, String> timeRangeMap = parseTimeRange(timeRange);
        String startTime = timeRangeMap.get("startTime");
        String endTime = timeRangeMap.get("endTime");
        
        // 获取基础统计数据
        Integer totalDocuments = dashboardMapper.getTotalDocuments(startTime, endTime);
        Integer documentsWithErrors = dashboardMapper.getDocumentsWithErrors(startTime, endTime);
        Integer totalErrors = dashboardMapper.getTotalErrors(startTime, endTime);
        Integer monitoredSites = dashboardMapper.getMonitoredSites();
        
        // 获取网站状态数据
        List<Map<String, Object>> siteStatusData = dashboardMapper.getSiteStatus();
        List<DashboardOverviewRespVO.SiteStatus> siteStatusList = siteStatusData.stream()
                .map(item -> {
                    DashboardOverviewRespVO.SiteStatus siteStatus = new DashboardOverviewRespVO.SiteStatus();
                    siteStatus.setId(((Number) item.get("id")).intValue());
                    siteStatus.setName((String) item.get("name"));
                    siteStatus.setUrl((String) item.get("url"));
                    siteStatus.setStatus((String) item.get("status"));
                    siteStatus.setLastPingTime(item.get("lastPingTime") != null ? 
                            DateUtil.formatDateTime((Date) item.get("lastPingTime")) : null);
                    return siteStatus;
                }).collect(Collectors.toList());
        
        // 获取文档趋势数据
        String groupBy = getGroupByFromTimeRange(timeRange);
        List<Map<String, Object>> documentTrendData = dashboardMapper.getDocumentTrend(startTime, endTime, groupBy);
        List<Map<String, Object>> errorRateTrendData = dashboardMapper.getErrorRateTrend(startTime, endTime, groupBy);
        
        List<DashboardOverviewRespVO.TrendPoint> documentTrend = documentTrendData.stream()
                .map(item -> {
                    DashboardOverviewRespVO.TrendPoint point = new DashboardOverviewRespVO.TrendPoint();
                    point.setDate((String) item.get("date"));
                    point.setValue(((Number) item.get("count")).intValue());
                    return point;
                }).collect(Collectors.toList());
        
        List<DashboardOverviewRespVO.TrendPoint> errorRateTrend = errorRateTrendData.stream()
                .map(item -> {
                    DashboardOverviewRespVO.TrendPoint point = new DashboardOverviewRespVO.TrendPoint();
                    point.setDate((String) item.get("date"));
                    point.setValue(((Number) item.get("error_rate")).doubleValue());
                    return point;
                }).collect(Collectors.toList());
        
        // 获取文档分布数据
        List<Map<String, Object>> documentDistributionData = dashboardMapper.getDocumentDistribution(startTime, endTime);
        List<DashboardOverviewRespVO.DistributionItem> documentDistribution = documentDistributionData.stream()
                .map(item -> {
                    DashboardOverviewRespVO.DistributionItem distributionItem = new DashboardOverviewRespVO.DistributionItem();
                    distributionItem.setName((String) item.get("name"));
                    distributionItem.setValue(((Number) item.get("count")).intValue());
                    return distributionItem;
                }).collect(Collectors.toList());
        
        // 构建返回对象
        DashboardOverviewRespVO respVO = new DashboardOverviewRespVO();
        respVO.setTotalDocuments(totalDocuments);
        respVO.setDocumentsWithErrors(documentsWithErrors);
        respVO.setTotalErrors(totalErrors);
        respVO.setMonitoredSites(monitoredSites);
        respVO.setErrorRate(totalDocuments > 0 ? (double) documentsWithErrors / totalDocuments * 100 : 0);
        respVO.setSiteStatus(siteStatusList);
        respVO.setDocumentTrend(documentTrend);
        respVO.setErrorRateTrend(errorRateTrend);
        respVO.setDocumentDistribution(documentDistribution);
        
        return respVO;
    }

    @Override
    public DashboardSitesMonitoringRespVO getSitesMonitoring(String timeRange, Integer siteId) {
        // 解析时间范围
        Map<String, String> timeRangeMap = parseTimeRange(timeRange);
        String startTime = timeRangeMap.get("startTime");
        String endTime = timeRangeMap.get("endTime");
        
        // 获取网站监测数据
        List<Map<String, Object>> sitesData = dashboardMapper.getSitesMonitoring(startTime, endTime, siteId);
        List<DashboardSitesMonitoringRespVO.SiteDetail> siteDetails = sitesData.stream()
                .map(item -> {
                    DashboardSitesMonitoringRespVO.SiteDetail detail = new DashboardSitesMonitoringRespVO.SiteDetail();
                    detail.setId(((Number) item.get("id")).intValue());
                    detail.setName((String) item.get("name"));
                    detail.setUrl((String) item.get("url"));
                    detail.setDocumentCount(((Number) item.get("documentCount")).intValue());
                    detail.setSuccessRate(((Number) item.get("successRate")).doubleValue());
                    detail.setAvgResponseTime(((Number) item.get("avgResponseTime")).doubleValue());
                    return detail;
                }).collect(Collectors.toList());
        
        // 获取可用性趋势
        String groupBy = getGroupByFromTimeRange(timeRange);
        List<Map<String, Object>> availabilityTrendData = dashboardMapper.getAvailabilityTrend(startTime, endTime, groupBy);
        List<DashboardSitesMonitoringRespVO.TrendPoint> availabilityTrend = availabilityTrendData.stream()
                .map(item -> {
                    DashboardSitesMonitoringRespVO.TrendPoint point = new DashboardSitesMonitoringRespVO.TrendPoint();
                    point.setDate((String) item.get("date"));
                    point.setValue(((Number) item.get("rate")).doubleValue());
                    return point;
                }).collect(Collectors.toList());
        
        // 构建返回对象
        DashboardSitesMonitoringRespVO respVO = new DashboardSitesMonitoringRespVO();
        respVO.setSites(siteDetails);
        respVO.setAvailabilityTrend(availabilityTrend);
        
        return respVO;
    }

    @Override
    public DashboardErrorAnalysisRespVO getErrorAnalysis(String timeRange, Long deptId) {
        // 解析时间范围
        Map<String, String> timeRangeMap = parseTimeRange(timeRange);
        String startTime = timeRangeMap.get("startTime");
        String endTime = timeRangeMap.get("endTime");
        
        // 获取错误类型分布
        List<Map<String, Object>> errorTypeData = dashboardMapper.getErrorTypeDistribution(startTime, endTime, deptId);
        List<DashboardErrorAnalysisRespVO.ErrorTypeItem> errorTypes = errorTypeData.stream()
                .map(item -> {
                    DashboardErrorAnalysisRespVO.ErrorTypeItem typeItem = new DashboardErrorAnalysisRespVO.ErrorTypeItem();
                    typeItem.setType((String) item.get("type"));
                    typeItem.setCount(((Number) item.get("count")).intValue());
                    return typeItem;
                }).collect(Collectors.toList());
        
        // 获取常见错误Top10
        List<Map<String, Object>> commonErrorsData = dashboardMapper.getCommonErrors(startTime, endTime, deptId);
        List<DashboardErrorAnalysisRespVO.CommonError> commonErrors = commonErrorsData.stream()
                .map(item -> {
                    DashboardErrorAnalysisRespVO.CommonError error = new DashboardErrorAnalysisRespVO.CommonError();
                    error.setWrongWord((String) item.get("wrongWord"));
                    error.setRightWord((String) item.get("rightWord"));
                    error.setCount(((Number) item.get("count")).intValue());
                    return error;
                }).collect(Collectors.toList());
        
        // 获取错误示例
        List<Map<String, Object>> errorExamplesData = dashboardMapper.getErrorExamples(startTime, endTime, deptId);
        List<DashboardErrorAnalysisRespVO.ErrorExample> errorExamples = errorExamplesData.stream()
                .map(item -> {
                    DashboardErrorAnalysisRespVO.ErrorExample example = new DashboardErrorAnalysisRespVO.ErrorExample();
                    example.setId(((Number) item.get("id")).longValue());
                    example.setWrongWord((String) item.get("wrongWord"));
                    example.setRightWord((String) item.get("rightWord"));
                    example.setContext((String) item.get("context"));
                    example.setDocumentTitle((String) item.get("documentTitle"));
                    return example;
                }).collect(Collectors.toList());
        
        // 构建返回对象
        DashboardErrorAnalysisRespVO respVO = new DashboardErrorAnalysisRespVO();
        respVO.setErrorTypes(errorTypes);
        respVO.setCommonErrors(commonErrors);
        respVO.setErrorExamples(errorExamples);
        
        return respVO;
    }

    @Override
    public DashboardDepartmentStatsRespVO getDepartmentStats(String timeRange) {
        // 解析时间范围
        Map<String, String> timeRangeMap = parseTimeRange(timeRange);
        String startTime = timeRangeMap.get("startTime");
        String endTime = timeRangeMap.get("endTime");
        
        // 获取部门工作情况
        List<Map<String, Object>> departmentStatsData = dashboardMapper.getDepartmentStats(startTime, endTime);
        List<DashboardDepartmentStatsRespVO.DepartmentStat> departmentStats = departmentStatsData.stream()
                .map(item -> {
                    DashboardDepartmentStatsRespVO.DepartmentStat stat = new DashboardDepartmentStatsRespVO.DepartmentStat();
                    stat.setId(((Number) item.get("id")).longValue());
                    stat.setName((String) item.get("name"));
                    stat.setDocumentCount(((Number) item.get("documentCount")).intValue());
                    stat.setErrorCount(((Number) item.get("errorCount")).intValue());
                    stat.setErrorRate(((Number) item.get("errorRate")).doubleValue());
                    stat.setAvgProcessingTime(((Number) item.get("avgProcessingTime")).doubleValue());
                    return stat;
                }).collect(Collectors.toList());
        
        // 构建返回对象
        DashboardDepartmentStatsRespVO respVO = new DashboardDepartmentStatsRespVO();
        respVO.setDepartments(departmentStats);
        
        return respVO;
    }

    @Override
    public DashboardTextQualityRespVO getTextQuality(String timeRange, Integer siteId) {
        // 解析时间范围
        Map<String, String> timeRangeMap = parseTimeRange(timeRange);
        String startTime = timeRangeMap.get("startTime");
        String endTime = timeRangeMap.get("endTime");
        
        // 获取文本质量趋势
        String groupBy = getGroupByFromTimeRange(timeRange);
        List<Map<String, Object>> qualityScoreTrendData = dashboardMapper.getQualityScoreTrend(startTime, endTime, groupBy, siteId);
        List<DashboardTextQualityRespVO.TrendPoint> qualityScoreTrend = qualityScoreTrendData.stream()
                .map(item -> {
                    DashboardTextQualityRespVO.TrendPoint point = new DashboardTextQualityRespVO.TrendPoint();
                    point.setDate((String) item.get("date"));
                    point.setValue(((Number) item.get("score")).doubleValue());
                    return point;
                }).collect(Collectors.toList());
        
        // 获取质量分布
        Map<String, Object> qualityDistributionData = dashboardMapper.getQualityDistribution(startTime, endTime, siteId);
        DashboardTextQualityRespVO.QualityDistribution qualityDistribution = new DashboardTextQualityRespVO.QualityDistribution();
        qualityDistribution.setExcellent(((Number) qualityDistributionData.get("excellent")).intValue());
        qualityDistribution.setGood(((Number) qualityDistributionData.get("good")).intValue());
        qualityDistribution.setAverage(((Number) qualityDistributionData.get("average")).intValue());
        qualityDistribution.setPoor(((Number) qualityDistributionData.get("poor")).intValue());
        
        // 获取优质文档示例
        List<Map<String, Object>> exemplaryDocumentsData = dashboardMapper.getExemplaryDocuments(startTime, endTime, siteId);
        List<DashboardTextQualityRespVO.ExemplaryDocument> exemplaryDocuments = exemplaryDocumentsData.stream()
                .map(item -> {
                    DashboardTextQualityRespVO.ExemplaryDocument doc = new DashboardTextQualityRespVO.ExemplaryDocument();
                    doc.setId(((Number) item.get("id")).longValue());
                    doc.setTitle((String) item.get("title"));
                    doc.setSource((String) item.get("source"));
                    doc.setDate((String) item.get("date"));
                    doc.setQualityScore(((Number) item.get("qualityScore")).intValue());
                    return doc;
                }).collect(Collectors.toList());
        
        // 构建返回对象
        DashboardTextQualityRespVO respVO = new DashboardTextQualityRespVO();
        respVO.setQualityScoreTrend(qualityScoreTrend);
        respVO.setQualityDistribution(qualityDistribution);
        respVO.setExemplaryDocuments(exemplaryDocuments);
        
        return respVO;
    }

    @Override
    public DashboardRealTimeStatsRespVO getRealTimeStats() {
        // 获取今日统计数据
        Integer todayDocuments = dashboardMapper.getTodayDocuments();
        Integer todayErrors = dashboardMapper.getTodayErrors();
        
        // 获取最新文档
        List<Map<String, Object>> latestDocumentsData = dashboardMapper.getLatestDocuments();
        List<DashboardRealTimeStatsRespVO.LatestDocument> latestDocuments = latestDocumentsData.stream()
                .map(item -> {
                    DashboardRealTimeStatsRespVO.LatestDocument doc = new DashboardRealTimeStatsRespVO.LatestDocument();
                    doc.setId(((Number) item.get("id")).longValue());
                    doc.setTitle((String) item.get("title"));
                    doc.setSource((String) item.get("source"));
                    doc.setTimestamp((String) item.get("timestamp"));
                    return doc;
                }).collect(Collectors.toList());
        
        // 获取最新错误
        List<Map<String, Object>> latestErrorsData = dashboardMapper.getLatestErrors();
        List<DashboardRealTimeStatsRespVO.LatestError> latestErrorsList = latestErrorsData.stream()
                .map(item -> {
                    DashboardRealTimeStatsRespVO.LatestError error = new DashboardRealTimeStatsRespVO.LatestError();
                    error.setId(((Number) item.get("id")).longValue());
                    error.setDocumentId(((Number) item.get("documentId")).longValue());
                    error.setDocumentTitle((String) item.get("documentTitle"));
                    error.setWrongWord((String) item.get("wrongWord"));
                    error.setRightWord((String) item.get("rightWord"));
                    error.setTimestamp((String) item.get("timestamp"));
                    return error;
                }).collect(Collectors.toList());
        
        // 构建返回对象
        DashboardRealTimeStatsRespVO respVO = new DashboardRealTimeStatsRespVO();
        respVO.setTodayDocuments(todayDocuments);
        respVO.setTodayErrors(todayErrors);
        respVO.setLatestDocuments(latestDocuments);
        respVO.setLatestErrors(latestErrorsList);
        
        return respVO;
    }

    @Override
    public DashboardTimeOptionsRespVO getTimeOptions() {
        // 获取最早记录时间和最晚记录时间
        String earliestTime = dashboardMapper.getEarliestRecordTime();
        String latestTime = dashboardMapper.getLatestRecordTime();
        
        // 构建时间选项
        List<DashboardTimeOptionsRespVO.Option> options = new ArrayList<>();
        options.add(new DashboardTimeOptionsRespVO.Option("today", "今天"));
        options.add(new DashboardTimeOptionsRespVO.Option("yesterday", "昨天"));
        options.add(new DashboardTimeOptionsRespVO.Option("last7days", "最近7天"));
        options.add(new DashboardTimeOptionsRespVO.Option("last30days", "最近30天"));
        options.add(new DashboardTimeOptionsRespVO.Option("thisMonth", "本月"));
        options.add(new DashboardTimeOptionsRespVO.Option("lastMonth", "上月"));
        options.add(new DashboardTimeOptionsRespVO.Option("thisYear", "今年"));
        options.add(new DashboardTimeOptionsRespVO.Option("custom", "自定义范围"));
        
        // 构建自定义范围
        DashboardTimeOptionsRespVO.CustomRange customRange = new DashboardTimeOptionsRespVO.CustomRange();
        customRange.setMin(earliestTime != null ? earliestTime.substring(0, 10) : "2023-01-01");
        customRange.setMax(latestTime != null ? latestTime.substring(0, 10) : DateUtil.today());
        
        // 构建返回对象
        DashboardTimeOptionsRespVO respVO = new DashboardTimeOptionsRespVO();
        respVO.setOptions(options);
        respVO.setCustomRange(customRange);
        
        return respVO;
    }

    @Override
    public DashboardFilterOptionsRespVO getFilterOptions() {
        // 获取部门列表
        List<Map<String, Object>> departmentsData = dashboardMapper.getAllDepartments();
        List<DashboardFilterOptionsRespVO.Option> departments = departmentsData.stream()
                .map(item -> {
                    DashboardFilterOptionsRespVO.Option option = new DashboardFilterOptionsRespVO.Option();
                    option.setValue(String.valueOf(((Number) item.get("id")).longValue()));
                    option.setLabel((String) item.get("name"));
                    return option;
                }).collect(Collectors.toList());
        
        // 获取网站列表
        List<Map<String, Object>> sitesData = dashboardMapper.getAllSites();
        List<DashboardFilterOptionsRespVO.Option> sites = sitesData.stream()
                .map(item -> {
                    DashboardFilterOptionsRespVO.Option option = new DashboardFilterOptionsRespVO.Option();
                    option.setValue(String.valueOf(((Number) item.get("id")).intValue()));
                    option.setLabel((String) item.get("name"));
                    return option;
                }).collect(Collectors.toList());
        
        // 获取错误类型列表
        List<Map<String, Object>> errorTypesData = dashboardMapper.getAllErrorTypes();
        List<DashboardFilterOptionsRespVO.Option> errorTypes = errorTypesData.stream()
                .map(item -> {
                    DashboardFilterOptionsRespVO.Option option = new DashboardFilterOptionsRespVO.Option();
                    option.setValue((String) item.get("type"));
                    option.setLabel((String) item.get("name"));
                    return option;
                }).collect(Collectors.toList());
        
        // 构建返回对象
        DashboardFilterOptionsRespVO respVO = new DashboardFilterOptionsRespVO();
        respVO.setDepartments(departments);
        respVO.setSites(sites);
        respVO.setErrorTypes(errorTypes);
        
        return respVO;
    }
    
    /**
     * 解析时间范围
     * 
     * @param timeRange 时间范围字符串
     * @return 开始时间和结束时间的Map
     */
    private Map<String, String> parseTimeRange(String timeRange) {
        Map<String, String> result = new HashMap<>();
        Date now = new Date();
        
        // 默认为最近7天
        String startTime = DateUtil.formatDateTime(DateUtil.offsetDay(now, -7));
        String endTime = DateUtil.formatDateTime(now);
        
        if (StrUtil.isNotEmpty(timeRange)) {
            switch (timeRange) {
                case "today":
                    startTime = DateUtil.formatDateTime(DateUtil.beginOfDay(now));
                    endTime = DateUtil.formatDateTime(DateUtil.endOfDay(now));
                    break;
                case "yesterday":
                    Date yesterday = DateUtil.offsetDay(now, -1);
                    startTime = DateUtil.formatDateTime(DateUtil.beginOfDay(yesterday));
                    endTime = DateUtil.formatDateTime(DateUtil.endOfDay(yesterday));
                    break;
                case "last7days":
                    startTime = DateUtil.formatDateTime(DateUtil.offsetDay(now, -7));
                    break;
                case "last30days":
                    startTime = DateUtil.formatDateTime(DateUtil.offsetDay(now, -30));
                    break;
                case "thisMonth":
                    startTime = DateUtil.formatDateTime(DateUtil.beginOfMonth(now));
                    endTime = DateUtil.formatDateTime(DateUtil.endOfMonth(now));
                    break;
                case "lastMonth":
                    Date lastMonth = DateUtil.offsetMonth(now, -1);
                    startTime = DateUtil.formatDateTime(DateUtil.beginOfMonth(lastMonth));
                    endTime = DateUtil.formatDateTime(DateUtil.endOfMonth(lastMonth));
                    break;
                case "thisYear":
                    startTime = DateUtil.formatDateTime(DateUtil.beginOfYear(now));
                    endTime = DateUtil.formatDateTime(DateUtil.endOfYear(now));
                    break;
                default:
                    // 自定义范围, 格式为: custom:2023-01-01_2023-12-31
                    if (timeRange.startsWith("custom:")) {
                        String[] customRange = timeRange.substring(7).split("_");
                        if (customRange.length == 2) {
                            startTime = customRange[0] + " 00:00:00";
                            endTime = customRange[1] + " 23:59:59";
                        }
                    }
                    break;
            }
        }
        
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        return result;
    }
    
    /**
     * 根据时间范围确定分组方式
     * 
     * @param timeRange 时间范围字符串
     * @return 分组方式 (day, week, month)
     */
    private String getGroupByFromTimeRange(String timeRange) {
        if (StrUtil.isEmpty(timeRange)) {
            return "day";
        }
        
        // 根据时间范围选择合适的分组方式
        if (timeRange.equals("today") || timeRange.equals("yesterday")) {
            return "hour";
        } else if (timeRange.equals("last7days") || timeRange.equals("thisMonth")) {
            return "day";
        } else if (timeRange.equals("last30days") || timeRange.equals("lastMonth")) {
            return "day";
        } else if (timeRange.equals("thisYear")) {
            return "month";
        } else if (timeRange.startsWith("custom:")) {
            String[] customRange = timeRange.substring(7).split("_");
            if (customRange.length == 2) {
                // 计算天数差异
                Date startDate = DateUtil.parseDate(customRange[0]);
                Date endDate = DateUtil.parseDate(customRange[1]);
                long daysDiff = DateUtil.betweenDay(startDate, endDate, true);
                
                if (daysDiff <= 2) {
                    return "hour";
                } else if (daysDiff <= 60) {
                    return "day";
                } else if (daysDiff <= 365) {
                    return "week";
                } else {
                    return "month";
                }
            }
        }
        
        return "day";  // 默认按天分组
    }
} 