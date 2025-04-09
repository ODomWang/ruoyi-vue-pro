package cn.iocoder.yudao.module.wenxun.controller.admin.stats;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.*;
import cn.iocoder.yudao.module.wenxun.service.stats.WenxunSpiderStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 文巡爬虫统计")
@RestController
@RequestMapping("/wenxun/stats/spider")
@Validated
public class WenxunSpiderStatsController {
    
    @Resource
    private WenxunSpiderStatsService spiderStatsService;
    
    @GetMapping("/summary")
    @Operation(summary = "获取爬虫数据汇总统计")
     public CommonResult<SpiderStatsRespVO> getSpiderStatsSummary() {
        return success(spiderStatsService.getSpiderStatsSummary());
    }
    
    @PostMapping("/dimension")
    @Operation(summary = "获取爬虫数据多维度统计")
     public CommonResult<SpiderStatsRespVO> getSpiderStatsByDimension(@Validated @RequestBody StatsBaseReqVO reqVO) {
        return success(spiderStatsService.getSpiderStatsByDimension(reqVO));
    }

    @GetMapping("/count")
    @Operation(summary = "获取爬虫数据量统计")
     public CommonResult<SpiderCountRespVO> getSpiderCount() {
        return success(spiderStatsService.getSpiderCount());
    }

    @GetMapping("/success-rate")
    @Operation(summary = "获取爬虫采集成功率统计")
     public CommonResult<SpiderSuccessRateRespVO> getSpiderSuccessRate() {
        return success(spiderStatsService.getSpiderSuccessRate());
    }

    @GetMapping("/trend")
    @Operation(summary = "获取爬虫采集趋势统计")
     public CommonResult<List<SpiderTrendRespVO>> getSpiderTrend() {
        return success(spiderStatsService.getSpiderTrend());
    }

    @GetMapping("/source")
    @Operation(summary = "获取爬虫来源分布统计")
     public CommonResult<List<SpiderSourceRespVO>> getSpiderSource() {
        return success(spiderStatsService.getSpiderSource());
    }
} 