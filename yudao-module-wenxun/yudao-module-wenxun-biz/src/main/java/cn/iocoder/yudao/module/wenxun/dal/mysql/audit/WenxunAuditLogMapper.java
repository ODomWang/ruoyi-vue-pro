package cn.iocoder.yudao.module.wenxun.dal.mysql.audit;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.audit.WenxunAuditLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 审核日志 Mapper
 */
@Mapper
@Repository("wenxunAuditLogMapper")
public interface WenxunAuditLogMapper extends BaseMapperX<WenxunAuditLogDO> {

    /**
     * 统计审核日志总数
     *
     * @param deptId 部门ID
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param creator 创建者
     * @return 统计结果
     */
    Integer countAuditLogs(@Param("deptId") Long deptId,
                         @Param("beginTime") LocalDateTime beginTime,
                         @Param("endTime") LocalDateTime endTime,
                         @Param("creator") String creator);

    /**
     * 按状态统计审核日志
     *
     * @param status 状态
     * @param deptId 部门ID
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param creator 创建者
     * @return 统计结果
     */
    Integer countAuditLogsByStatus(@Param("status") Integer status,
                                @Param("deptId") Long deptId,
                                @Param("beginTime") LocalDateTime beginTime,
                                @Param("endTime") LocalDateTime endTime,
                                @Param("creator") String creator);

    /**
     * 获取部门审核分布数据
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 部门分布数据
     */
    List<Map<String, Object>> getDeptDistribution(@Param("beginTime") LocalDateTime beginTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 获取最近审核趋势数据
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param deptId 部门ID
     * @param creator 创建者
     * @return 趋势数据
     */
    List<Map<String, Object>> getRecentAuditTrend(@Param("beginTime") LocalDateTime beginTime,
                                               @Param("endTime") LocalDateTime endTime,
                                               @Param("deptId") Long deptId,
                                               @Param("creator") String creator);
} 