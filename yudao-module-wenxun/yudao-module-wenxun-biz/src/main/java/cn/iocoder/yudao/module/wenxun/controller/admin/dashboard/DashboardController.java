package cn.iocoder.yudao.module.wenxun.controller.admin.dashboard;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.dashboard.vo.*;
import cn.iocoder.yudao.module.wenxun.service.wenxun.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 数字大屏
 */
@Tag(name = "管理后台 - 数字大屏")
@RestController
@RequestMapping("/wenxun/dashboard")
@Validated
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/overview")
    @Operation(summary = "获取概览数据")
    public CommonResult<DashboardOverviewRespVO> getOverview(
            @Parameter(description = "时间范围", example = "last7days") @RequestParam(required = false) String timeRange) {
        return success(dashboardService.getOverview(timeRange));
    }

    @GetMapping("/sites-monitoring")
    @Operation(summary = "获取网站监测数据")
    public CommonResult<DashboardSitesMonitoringRespVO> getSitesMonitoring(
            @Parameter(description = "时间范围", example = "last7days") @RequestParam(required = false) String timeRange,
            @Parameter(description = "网站ID", example = "1") @RequestParam(required = false) Integer siteId) {
        return success(dashboardService.getSitesMonitoring(timeRange, siteId));
    }

    @GetMapping("/error-analysis")
    @Operation(summary = "获取错误分析数据")
    public CommonResult<DashboardErrorAnalysisRespVO> getErrorAnalysis(
            @Parameter(description = "时间范围", example = "last7days") @RequestParam(required = false) String timeRange,
            @Parameter(description = "部门ID", example = "1") @RequestParam(required = false) Long deptId) {
        return success(dashboardService.getErrorAnalysis(timeRange, deptId));
    }

    @GetMapping("/department-stats")
    @Operation(summary = "获取部门工作统计数据")
    public CommonResult<DashboardDepartmentStatsRespVO> getDepartmentStats(
            @Parameter(description = "时间范围", example = "last7days") @RequestParam(required = false) String timeRange) {
        return success(dashboardService.getDepartmentStats(timeRange));
    }

    @GetMapping("/text-quality")
    @Operation(summary = "获取文本质量数据")
    public CommonResult<DashboardTextQualityRespVO> getTextQuality(
            @Parameter(description = "时间范围", example = "last7days") @RequestParam(required = false) String timeRange,
            @Parameter(description = "网站ID", example = "1") @RequestParam(required = false) Integer siteId) {
        return success(dashboardService.getTextQuality(timeRange, siteId));
    }

    @GetMapping("/real-time-stats")
    @Operation(summary = "获取实时统计数据")
    public CommonResult<DashboardRealTimeStatsRespVO> getRealTimeStats() {
        return success(dashboardService.getRealTimeStats());
    }

    @GetMapping("/time-options")
    @Operation(summary = "获取时间选项数据")
    public CommonResult<DashboardTimeOptionsRespVO> getTimeOptions() {
        return success(dashboardService.getTimeOptions());
    }

    @GetMapping("/filter-options")
    @Operation(summary = "获取筛选选项数据")
    public CommonResult<DashboardFilterOptionsRespVO> getFilterOptions() {
        return success(dashboardService.getFilterOptions());
    }
} 