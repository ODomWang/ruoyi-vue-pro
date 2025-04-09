package cn.iocoder.yudao.module.wenxun.service.stats.impl;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.AuditStatsReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.AuditStatsRespVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.audit.WenxunAuditLogDO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.audit.WenxunCustomerAuditLogDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.audit.WenxunAuditLogMapper;
import cn.iocoder.yudao.module.wenxun.dal.mysql.audit.WenxunCustomerAuditLogMapper;
import cn.iocoder.yudao.module.wenxun.service.stats.WenxunAuditStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文巡审核统计 Service 实现类
 */
@Service
@Slf4j
public class WenxunAuditStatsServiceImpl implements WenxunAuditStatsService {

    @Resource(name = "wenxunAuditLogMapper")
    private WenxunAuditLogMapper auditLogMapper;
    
    @Resource(name = "wenxunCustomerAuditLogMapper")
    private WenxunCustomerAuditLogMapper customerAuditLogMapper;
    
    @Resource
    private AdminUserApi adminUserApi;

    @Override
    public AuditStatsRespVO getAuditStats(AuditStatsReqVO reqVO) {
        // 管理员可查看所有审核数据
        AuditStatsRespVO respVO = new AuditStatsRespVO();
        
        // 管理员审核日志基本统计信息
        Integer adminTotalCount = auditLogMapper.countAuditLogs(reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), null);
        Integer adminApprovedCount = auditLogMapper.countAuditLogsByStatus(1, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), null);
        Integer adminRejectedCount = auditLogMapper.countAuditLogsByStatus(2, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), null);
        Integer adminPendingCount = auditLogMapper.countAuditLogsByStatus(0, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), null);
        
        // 客户审核日志基本统计信息
        Integer customerTotalCount = customerAuditLogMapper.countCustomerAuditLogs(reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), null);
        Integer customerApprovedCount = customerAuditLogMapper.countCustomerAuditLogsByStatus(1, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), null);
        Integer customerRejectedCount = customerAuditLogMapper.countCustomerAuditLogsByStatus(2, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), null);
        Integer customerPendingCount = customerAuditLogMapper.countCustomerAuditLogsByStatus(0, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), null);
        
        // 合并统计结果
        Integer totalCount = adminTotalCount + customerTotalCount;
        Integer approvedCount = adminApprovedCount + customerApprovedCount;
        Integer rejectedCount = adminRejectedCount + customerRejectedCount;
        Integer pendingCount = adminPendingCount + customerPendingCount;
        
        respVO.setTotalCount(totalCount);
        respVO.setApprovedCount(approvedCount);
        respVO.setRejectedCount(rejectedCount);
        respVO.setPendingCount(pendingCount);
        
        // 计算通过率
        if (totalCount > 0) {
            respVO.setApprovalRate((double) approvedCount / totalCount);
        } else {
            respVO.setApprovalRate(0.0);
        }
        
        // 状态分布统计
        List<Map<String, Object>> statusDistribution = new ArrayList<>();
        Map<String, Object> approvedMap = new HashMap<>();
        approvedMap.put("status", "已通过");
        approvedMap.put("count", approvedCount);
        approvedMap.put("percentage", totalCount > 0 ? (double) approvedCount / totalCount : 0);
        
        Map<String, Object> rejectedMap = new HashMap<>();
        rejectedMap.put("status", "已驳回");
        rejectedMap.put("count", rejectedCount);
        rejectedMap.put("percentage", totalCount > 0 ? (double) rejectedCount / totalCount : 0);
        
        Map<String, Object> pendingMap = new HashMap<>();
        pendingMap.put("status", "待审核");
        pendingMap.put("count", pendingCount);
        pendingMap.put("percentage", totalCount > 0 ? (double) pendingCount / totalCount : 0);
        
        statusDistribution.add(approvedMap);
        statusDistribution.add(rejectedMap);
        statusDistribution.add(pendingMap);
        respVO.setStatusDistribution(statusDistribution);
        
        // 部门审核分布统计 (合并管理员和客户的部门分布)
        List<Map<String, Object>> adminDeptDistribution = auditLogMapper.getDeptDistribution(
                reqVO.getBeginTime(), reqVO.getEndTime());
        List<Map<String, Object>> customerDeptDistribution = customerAuditLogMapper.getCustomerDeptDistribution(
                reqVO.getBeginTime(), reqVO.getEndTime());
        
        // 合并部门分布数据
        Map<Long, Map<String, Object>> deptMap = new HashMap<>();
        
        for (Map<String, Object> dept : adminDeptDistribution) {
            Long deptId = (Long) dept.get("deptId");
            deptMap.put(deptId, dept);
        }
        
        for (Map<String, Object> dept : customerDeptDistribution) {
            Long deptId = (Long) dept.get("deptId");
            if (deptMap.containsKey(deptId)) {
                // 合并统计数据
                Map<String, Object> existingDept = deptMap.get(deptId);
                int adminTotal = (int) existingDept.get("totalCount");
                int customerTotal = (int) dept.get("totalCount");
                existingDept.put("totalCount", adminTotal + customerTotal);
                
                int adminApproved = (int) existingDept.get("approvedCount");
                int customerApproved = (int) dept.get("approvedCount");
                existingDept.put("approvedCount", adminApproved + customerApproved);
                
                int adminRejected = (int) existingDept.get("rejectedCount");
                int customerRejected = (int) dept.get("rejectedCount");
                existingDept.put("rejectedCount", adminRejected + customerRejected);
                
                int adminPending = (int) existingDept.get("pendingCount");
                int customerPending = (int) dept.get("pendingCount");
                existingDept.put("pendingCount", adminPending + customerPending);
            } else {
                deptMap.put(deptId, dept);
            }
        }
        
        List<Map<String, Object>> mergedDeptDistribution = new ArrayList<>(deptMap.values());
        respVO.setDeptDistribution(mergedDeptDistribution);
        
        // 最近七天审核趋势 (合并管理员和客户的趋势数据)
        List<Map<String, Object>> adminRecentTrend = auditLogMapper.getRecentAuditTrend(
                LocalDateTime.now().minusDays(6), LocalDateTime.now(), reqVO.getDeptId(), null);
        List<Map<String, Object>> customerRecentTrend = customerAuditLogMapper.getRecentCustomerAuditTrend(
                LocalDateTime.now().minusDays(6), LocalDateTime.now(), reqVO.getDeptId(), null);
        
        // 合并趋势数据
        Map<String, Map<String, Object>> trendMap = new HashMap<>();
        
        for (Map<String, Object> trend : adminRecentTrend) {
            String date = (String) trend.get("date");
            trendMap.put(date, trend);
        }
        
        for (Map<String, Object> trend : customerRecentTrend) {
            String date = (String) trend.get("date");
            if (trendMap.containsKey(date)) {
                // 合并统计数据
                Map<String, Object> existingTrend = trendMap.get(date);
                int adminTotal = (int) existingTrend.get("totalCount");
                int customerTotal = (int) trend.get("totalCount");
                existingTrend.put("totalCount", adminTotal + customerTotal);
                
                int adminApproved = (int) existingTrend.get("approvedCount");
                int customerApproved = (int) trend.get("approvedCount");
                existingTrend.put("approvedCount", adminApproved + customerApproved);
                
                int adminRejected = (int) existingTrend.get("rejectedCount");
                int customerRejected = (int) trend.get("rejectedCount");
                existingTrend.put("rejectedCount", adminRejected + customerRejected);
                
                int adminPending = (int) existingTrend.get("pendingCount");
                int customerPending = (int) trend.get("pendingCount");
                existingTrend.put("pendingCount", adminPending + customerPending);
            } else {
                trendMap.put(date, trend);
            }
        }
        
        List<Map<String, Object>> mergedRecentTrend = new ArrayList<>(trendMap.values());
        // 按日期排序
        mergedRecentTrend.sort((a, b) -> ((String) a.get("date")).compareTo((String) b.get("date")));
        respVO.setRecentTrend(mergedRecentTrend);
        
        return respVO;
    }

    @Override
    public AuditStatsRespVO getUserAuditStats(AuditStatsReqVO reqVO) {
        // 普通用户只能查看自己的审核数据
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        String username = adminUserApi.getUser(userId).getNickname();
        
        AuditStatsRespVO respVO = new AuditStatsRespVO();
        
        // 管理员审核日志基本统计信息
        Integer adminTotalCount = auditLogMapper.countAuditLogs(reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), username);
        Integer adminApprovedCount = auditLogMapper.countAuditLogsByStatus(1, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), username);
        Integer adminRejectedCount = auditLogMapper.countAuditLogsByStatus(2, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), username);
        Integer adminPendingCount = auditLogMapper.countAuditLogsByStatus(0, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), username);
        
        // 客户审核日志基本统计信息
        Integer customerTotalCount = customerAuditLogMapper.countCustomerAuditLogs(reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), username);
        Integer customerApprovedCount = customerAuditLogMapper.countCustomerAuditLogsByStatus(1, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), username);
        Integer customerRejectedCount = customerAuditLogMapper.countCustomerAuditLogsByStatus(2, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), username);
        Integer customerPendingCount = customerAuditLogMapper.countCustomerAuditLogsByStatus(0, reqVO.getDeptId(), reqVO.getBeginTime(), reqVO.getEndTime(), username);
        
        // 合并统计结果
        Integer totalCount = adminTotalCount + customerTotalCount;
        Integer approvedCount = adminApprovedCount + customerApprovedCount;
        Integer rejectedCount = adminRejectedCount + customerRejectedCount;
        Integer pendingCount = adminPendingCount + customerPendingCount;
        
        respVO.setTotalCount(totalCount);
        respVO.setApprovedCount(approvedCount);
        respVO.setRejectedCount(rejectedCount);
        respVO.setPendingCount(pendingCount);
        
        // 计算通过率
        if (totalCount > 0) {
            respVO.setApprovalRate((double) approvedCount / totalCount);
        } else {
            respVO.setApprovalRate(0.0);
        }
        
        // 状态分布统计
        List<Map<String, Object>> statusDistribution = new ArrayList<>();
        Map<String, Object> approvedMap = new HashMap<>();
        approvedMap.put("status", "已通过");
        approvedMap.put("count", approvedCount);
        approvedMap.put("percentage", totalCount > 0 ? (double) approvedCount / totalCount : 0);
        
        Map<String, Object> rejectedMap = new HashMap<>();
        rejectedMap.put("status", "已驳回");
        rejectedMap.put("count", rejectedCount);
        rejectedMap.put("percentage", totalCount > 0 ? (double) rejectedCount / totalCount : 0);
        
        Map<String, Object> pendingMap = new HashMap<>();
        pendingMap.put("status", "待审核");
        pendingMap.put("count", pendingCount);
        pendingMap.put("percentage", totalCount > 0 ? (double) pendingCount / totalCount : 0);
        
        statusDistribution.add(approvedMap);
        statusDistribution.add(rejectedMap);
        statusDistribution.add(pendingMap);
        respVO.setStatusDistribution(statusDistribution);
        
        // 最近七天审核趋势 (合并管理员和客户的趋势数据)
        List<Map<String, Object>> adminRecentTrend = auditLogMapper.getRecentAuditTrend(
                LocalDateTime.now().minusDays(6), LocalDateTime.now(), reqVO.getDeptId(), username);
        List<Map<String, Object>> customerRecentTrend = customerAuditLogMapper.getRecentCustomerAuditTrend(
                LocalDateTime.now().minusDays(6), LocalDateTime.now(), reqVO.getDeptId(), username);
        
        // 合并趋势数据
        Map<String, Map<String, Object>> trendMap = new HashMap<>();
        
        for (Map<String, Object> trend : adminRecentTrend) {
            String date = (String) trend.get("date");
            trendMap.put(date, trend);
        }
        
        for (Map<String, Object> trend : customerRecentTrend) {
            String date = (String) trend.get("date");
            if (trendMap.containsKey(date)) {
                // 合并统计数据
                Map<String, Object> existingTrend = trendMap.get(date);
                int adminTotal = (int) existingTrend.get("totalCount");
                int customerTotal = (int) trend.get("totalCount");
                existingTrend.put("totalCount", adminTotal + customerTotal);
                
                int adminApproved = (int) existingTrend.get("approvedCount");
                int customerApproved = (int) trend.get("approvedCount");
                existingTrend.put("approvedCount", adminApproved + customerApproved);
                
                int adminRejected = (int) existingTrend.get("rejectedCount");
                int customerRejected = (int) trend.get("rejectedCount");
                existingTrend.put("rejectedCount", adminRejected + customerRejected);
                
                int adminPending = (int) existingTrend.get("pendingCount");
                int customerPending = (int) trend.get("pendingCount");
                existingTrend.put("pendingCount", adminPending + customerPending);
            } else {
                trendMap.put(date, trend);
            }
        }
        
        List<Map<String, Object>> mergedRecentTrend = new ArrayList<>(trendMap.values());
        // 按日期排序
        mergedRecentTrend.sort((a, b) -> ((String) a.get("date")).compareTo((String) b.get("date")));
        respVO.setRecentTrend(mergedRecentTrend);
        
        // 用户不需要部门分布数据
        respVO.setDeptDistribution(new ArrayList<>());
        
        return respVO;
    }
} 