package cn.iocoder.yudao.module.wenxun.dal.mysql.audit;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.audit.WenxunCustomerAuditLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 客户审核日志 Mapper
 */
@Mapper
@Repository("wenxunCustomerAuditLogMapper")
public interface WenxunCustomerAuditLogMapper extends BaseMapperX<WenxunCustomerAuditLogDO> {

    /**
     * 统计客户审核日志总数
     *
     * @param deptId 部门ID
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param auditor 审核人
     * @return 统计结果
     */
    Integer countCustomerAuditLogs(@Param("deptId") Long deptId,
                               @Param("beginTime") LocalDateTime beginTime,
                               @Param("endTime") LocalDateTime endTime,
                               @Param("auditor") String auditor);

    /**
     * 按状态统计客户审核日志
     *
     * @param status 状态
     * @param deptId 部门ID
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param auditor 审核人
     * @return 统计结果
     */
    Integer countCustomerAuditLogsByStatus(@Param("status") Integer status,
                                      @Param("deptId") Long deptId,
                                      @Param("beginTime") LocalDateTime beginTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      @Param("auditor") String auditor);

    /**
     * 获取客户审核部门分布数据
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 部门分布数据
     */
    List<Map<String, Object>> getCustomerDeptDistribution(@Param("beginTime") LocalDateTime beginTime,
                                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 获取客户审核最近趋势数据
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param deptId 部门ID
     * @param auditor 审核人
     * @return 趋势数据
     */
    List<Map<String, Object>> getRecentCustomerAuditTrend(@Param("beginTime") LocalDateTime beginTime,
                                                     @Param("endTime") LocalDateTime endTime,
                                                     @Param("deptId") Long deptId,
                                                     @Param("auditor") String auditor);
} 