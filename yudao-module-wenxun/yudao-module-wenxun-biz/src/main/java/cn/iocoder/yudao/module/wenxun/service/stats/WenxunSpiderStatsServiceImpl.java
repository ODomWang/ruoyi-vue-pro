package cn.iocoder.yudao.module.wenxun.service.stats;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.system.api.permission.dto.DeptDataPermissionRespDTO;
import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.spider.WenxunSpiderCrawlDetailDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.spider.WenxunSpiderCrawlDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 文巡爬虫统计 Service 实现类
 */
@Service
@Validated
public class WenxunSpiderStatsServiceImpl implements WenxunSpiderStatsService {

    @Resource
    private WenxunSpiderCrawlDetailMapper spiderCrawlDetailMapper;

    @Resource
    private PermissionApi permissionApi;

    @Resource
    private DeptApi deptApi;

    /**
     * 获取用户数据权限范围的部门编号列表
     * 如果是管理员，则返回所有部门ID；如果是普通用户，则返回其所在部门及下级部门的ID
     *
     * @return 部门编号列表
     */
    private Set<Long> getPermissionDeptIds() {
        DeptDataPermissionRespDTO deptDataPermission = permissionApi.getDeptDataPermission(SecurityFrameworkUtils.getLoginUserId());
        return deptDataPermission.getDeptIds();
    }

    @Override
    public SpiderStatsRespVO getSpiderStatsSummary() {
        // 1. 获取用户数据权限范围的部门编号列表
        Set<Long> deptIds = getPermissionDeptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return new SpiderStatsRespVO();
        }

        // 2. 构建基础查询条件
        LambdaQueryWrapperX<WenxunSpiderCrawlDetailDO> baseWrapper = new LambdaQueryWrapperX<WenxunSpiderCrawlDetailDO>()
                .eq(WenxunSpiderCrawlDetailDO::getDeleted, false)
                .in(WenxunSpiderCrawlDetailDO::getDeptId, deptIds);

        // 3. 获取总数据量
        Long totalCount = spiderCrawlDetailMapper.selectCount(baseWrapper);
        if (totalCount == 0) {
            return new SpiderStatsRespVO();
        }

        // 4. 获取成功数据量
        Long totalSuccessCount = spiderCrawlDetailMapper.selectCount(baseWrapper.clone()
                .eq(WenxunSpiderCrawlDetailDO::getStatus, true));

        // 5. 计算成功率
        BigDecimal totalSuccessRate = calculateSuccessRate(totalSuccessCount, totalCount);

        // 6. 获取今日数据量
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Long todayCount = spiderCrawlDetailMapper.selectCount(baseWrapper.clone()
                .between(WenxunSpiderCrawlDetailDO::getCreateTime, today, LocalDateTime.now()));

        // 7. 获取本周数据量
        LocalDateTime weekStart = LocalDateTime.now().with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0);
        Long weekCount = spiderCrawlDetailMapper.selectCount(baseWrapper.clone()
                .between(WenxunSpiderCrawlDetailDO::getCreateTime, weekStart, LocalDateTime.now()));

        // 8. 获取本月数据量
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        Long monthCount = spiderCrawlDetailMapper.selectCount(baseWrapper.clone()
                .between(WenxunSpiderCrawlDetailDO::getCreateTime, monthStart, LocalDateTime.now()));

        // 9. 获取部门维度统计
        List<SpiderStatsRespVO.DeptStatVO> deptStats = getDeptStats(deptIds, monthStart, LocalDateTime.now());

        // 10. 拼接返回
        SpiderStatsRespVO respVO = new SpiderStatsRespVO();
        respVO.setTotalCount(totalCount);
        respVO.setTodayCount(todayCount);
        respVO.setWeekCount(weekCount);
        respVO.setMonthCount(monthCount);
        respVO.setDeptStats(deptStats);
        return respVO;
    }

    private List<SpiderStatsRespVO.DeptStatVO> getDeptStats(Set<Long> deptIds, LocalDateTime start, LocalDateTime end) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }

        // 1. 查询部门数据
        List<DeptRespDTO> deptList = deptApi.getDeptList(deptIds);
        if (CollUtil.isEmpty(deptList)) {
            return Collections.emptyList();
        }

        // 2. 统计各部门数据量
        List<Map<String, Object>> deptStatsList = spiderCrawlDetailMapper.selectCountByTimeRangeAndDimension(
                start, end, "dept_id", deptIds);
        Map<Long, Long> deptCountMap = deptStatsList.stream()
                .collect(Collectors.toMap(
                        stat -> Long.valueOf(String.valueOf(stat.get("dimension_key"))),
                        stat -> Long.valueOf(String.valueOf(stat.get("count")))
                ));

        // 3. 组装数据
        return deptList.stream().map(dept -> {
            SpiderStatsRespVO.DeptStatVO statVO = new SpiderStatsRespVO.DeptStatVO();
            statVO.setDeptId(dept.getId());
            statVO.setDeptName(dept.getName());
            statVO.setCount(deptCountMap.getOrDefault(dept.getId(), 0L));
            return statVO;
        }).collect(Collectors.toList());
    }

    @Override
    public SpiderStatsRespVO getSpiderStatsByDimension(StatsBaseReqVO reqVO) {
        // 1. 获取用户数据权限范围的部门编号列表
        Set<Long> deptIds = getPermissionDeptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return new SpiderStatsRespVO();
        }

        // 2. 构建查询条件
        LambdaQueryWrapperX<WenxunSpiderCrawlDetailDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.eq(WenxunSpiderCrawlDetailDO::getDeleted, false)
                .in(WenxunSpiderCrawlDetailDO::getDeptId, deptIds)
                .between(WenxunSpiderCrawlDetailDO::getCreateTime, reqVO.getStartTime(), reqVO.getEndTime());

        // 3. 查询统计
        Long count = spiderCrawlDetailMapper.selectCount(wrapper);

        // 4. 获取部门维度统计
        List<SpiderStatsRespVO.DeptStatVO> deptStats = getDeptStats(deptIds, reqVO.getStartTime(), reqVO.getEndTime());

        // 5. 拼接返回
        SpiderStatsRespVO respVO = new SpiderStatsRespVO();
        respVO.setTotalCount(count);
        respVO.setDeptStats(deptStats);
        return respVO;
    }

    @Override
    public SpiderCountRespVO getSpiderCount() {
        // 1. 获取用户数据权限范围的部门编号列表
        Set<Long> deptIds = getPermissionDeptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return new SpiderCountRespVO();
        }

        // 2. 构建基础查询条件
        LambdaQueryWrapperX<WenxunSpiderCrawlDetailDO> baseWrapper = new LambdaQueryWrapperX<WenxunSpiderCrawlDetailDO>()
                .eq(WenxunSpiderCrawlDetailDO::getDeleted, false)
                .in(WenxunSpiderCrawlDetailDO::getDeptId, deptIds);

        // 3. 获取总数据量
        Long totalCount = spiderCrawlDetailMapper.selectCount(baseWrapper);

        // 4. 获取今日数据量
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Long todayCount = spiderCrawlDetailMapper.selectCount(baseWrapper.clone()
                .between(WenxunSpiderCrawlDetailDO::getCreateTime, today, LocalDateTime.now()));

        // 5. 获取本周数据量
        LocalDateTime weekStart = LocalDateTime.now().with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0);
        Long weekCount = spiderCrawlDetailMapper.selectCount(baseWrapper.clone()
                .between(WenxunSpiderCrawlDetailDO::getCreateTime, weekStart, LocalDateTime.now()));

        // 6. 获取本月数据量
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        Long monthCount = spiderCrawlDetailMapper.selectCount(baseWrapper.clone()
                .between(WenxunSpiderCrawlDetailDO::getCreateTime, monthStart, LocalDateTime.now()));

        // 7. 拼接返回
        SpiderCountRespVO respVO = new SpiderCountRespVO();
        respVO.setTotalCount(totalCount);
        respVO.setTodayCount(todayCount);
        respVO.setWeekCount(weekCount);
        respVO.setMonthCount(monthCount);
        return respVO;
    }

    @Override
    public SpiderSuccessRateRespVO getSpiderSuccessRate() {
        // 1. 获取用户数据权限范围的部门编号列表
        Set<Long> deptIds = getPermissionDeptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return new SpiderSuccessRateRespVO();
        }

        // 2. 构建基础查询条件
        LambdaQueryWrapperX<WenxunSpiderCrawlDetailDO> baseWrapper = new LambdaQueryWrapperX<WenxunSpiderCrawlDetailDO>()
                .eq(WenxunSpiderCrawlDetailDO::getDeleted, false)
                .in(WenxunSpiderCrawlDetailDO::getDeptId, deptIds);

        // 3. 计算总体成功率
        Long totalCount = spiderCrawlDetailMapper.selectCount(baseWrapper);
        if (totalCount == 0) {
            return new SpiderSuccessRateRespVO();
        }
        Long totalSuccessCount = spiderCrawlDetailMapper.selectCount(baseWrapper.clone()
                .eq(WenxunSpiderCrawlDetailDO::getStatus, 0));
        BigDecimal totalSuccessRate = calculateSuccessRate(totalSuccessCount, totalCount);

        // 4. 计算今日成功率
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        QueryWrapper<WenxunSpiderCrawlDetailDO> todayWrapper = new QueryWrapper<WenxunSpiderCrawlDetailDO>()
                .eq("deleted", false)
                .in("dept_id", deptIds)
                .between("create_time", today, LocalDateTime.now());
        Long todayCount = spiderCrawlDetailMapper.selectCount(todayWrapper);
        Long todaySuccessCount = spiderCrawlDetailMapper.selectCount(todayWrapper.clone()
                .eq("status", 0));
        BigDecimal todaySuccessRate = calculateSuccessRate(todaySuccessCount, todayCount);

        // 5. 计算本周成功率
        LocalDateTime weekStart = LocalDateTime.now().with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0);
        QueryWrapper<WenxunSpiderCrawlDetailDO> weekWrapper = new QueryWrapper<WenxunSpiderCrawlDetailDO>()
                .eq("deleted", false)
                .in("dept_id", deptIds)
                .between("create_time", weekStart, LocalDateTime.now());
        Long weekCount = spiderCrawlDetailMapper.selectCount(weekWrapper);
        Long weekSuccessCount = spiderCrawlDetailMapper.selectCount(weekWrapper.clone()
                .eq("status", 0));
        BigDecimal weekSuccessRate = calculateSuccessRate(weekSuccessCount, weekCount);

        // 6. 计算本月成功率
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        QueryWrapper<WenxunSpiderCrawlDetailDO> monthWrapper = new QueryWrapper<WenxunSpiderCrawlDetailDO>()
                .eq("deleted", false)
                .in("dept_id", deptIds)
                .between("create_time", monthStart, LocalDateTime.now());
        Long monthCount = spiderCrawlDetailMapper.selectCount(monthWrapper);
        Long monthSuccessCount = spiderCrawlDetailMapper.selectCount(monthWrapper.clone()
                .eq("status", 0));
        BigDecimal monthSuccessRate = calculateSuccessRate(monthSuccessCount, monthCount);

        // 7. 拼接返回
        SpiderSuccessRateRespVO respVO = new SpiderSuccessRateRespVO();
        respVO.setTotalSuccessRate(totalSuccessRate.longValue());
        respVO.setTodaySuccessRate(todaySuccessRate.longValue());
        respVO.setWeekSuccessRate(weekSuccessRate.longValue());
        respVO.setMonthSuccessRate(monthSuccessRate.longValue());
        return respVO;
    }

    /**
     * 计算成功率
     *
     * @param successCount 成功数量
     * @param totalCount 总数量
     * @return 成功率
     */
    private BigDecimal calculateSuccessRate(Long successCount, Long totalCount) {
        if (totalCount == null || totalCount == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(successCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
    }

    @Override
    public List<SpiderTrendRespVO> getSpiderTrend() {
        // 1. 获取用户数据权限范围的部门编号列表
        Set<Long> deptIds = getPermissionDeptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }

        // 2. 获取最近7天的日期列表
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(6).withHour(0).withMinute(0).withSecond(0);
        List<String> dateList = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startTime.plusDays(i).format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE))
                .collect(Collectors.toList());

        // 3. 查询趋势数据
        List<Map<String, Object>> trendDataList = spiderCrawlDetailMapper.selectCountGroupByTime(
                startTime, endTime, "%Y-%m-%d", deptIds);
        Map<String, Map<String, Object>> trendDataMap = trendDataList.stream()
                .collect(Collectors.toMap(data -> String.valueOf(data.get("time_key")), data -> data));

        // 4. 组装数据，补齐缺失日期
        return dateList.stream().map(date -> {
            Map<String, Object> trendData = trendDataMap.get(date);
            SpiderTrendRespVO trendVO = new SpiderTrendRespVO();
            trendVO.setTime(date);
            if (trendData != null) {
                trendVO.setCount(Long.valueOf(String.valueOf(trendData.getOrDefault("total_count", 0L))));
                trendVO.setSuccessCount(Long.valueOf(String.valueOf(trendData.getOrDefault("success_count", 0L))));
                trendVO.setFailCount(Long.valueOf(String.valueOf(trendData.getOrDefault("fail_count", 0L))));
            } else {
                trendVO.setCount(0L);
                trendVO.setSuccessCount(0L);
                trendVO.setFailCount(0L);
            }
            return trendVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SpiderSourceRespVO> getSpiderSource() {
        // 1. 获取用户数据权限范围的部门编号列表
        Set<Long> deptIds = getPermissionDeptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }

        // 2. 获取来源分布数据
        List<Map<String, Object>> sourceList = spiderCrawlDetailMapper.selectCountByTimeRangeAndDimension(
                null, null, "spider_config_id", deptIds);

        // 3. 计算总数
        long total = sourceList.stream()
                .mapToLong(source -> Long.parseLong(String.valueOf(source.get("count"))))
                .sum();

        // 4. 组装数据
        return sourceList.stream().map(source -> {
            SpiderSourceRespVO sourceVO = new SpiderSourceRespVO();
            sourceVO.setSourceName(String.valueOf(source.get("dimension_key")));
            Long count = Long.valueOf(String.valueOf(source.get("count")));
            sourceVO.setCount(count);
            sourceVO.setSuccessRate(BigDecimal.valueOf(count)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP));
            return sourceVO;
        }).collect(Collectors.toList());
    }
} 