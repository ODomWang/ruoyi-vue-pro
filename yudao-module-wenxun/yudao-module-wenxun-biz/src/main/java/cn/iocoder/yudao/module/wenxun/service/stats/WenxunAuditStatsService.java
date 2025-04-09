package cn.iocoder.yudao.module.wenxun.service.stats;

import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.AuditStatsReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.AuditStatsRespVO;

/**
 * 文巡审核统计 Service 接口
 */
public interface WenxunAuditStatsService {

    /**
     * 获取审核统计数据
     *
     * @param reqVO 查询条件
     * @return 统计结果
     */
    AuditStatsRespVO getAuditStats(AuditStatsReqVO reqVO);
    
    /**
     * 获取当前用户审核统计数据
     *
     * @param reqVO 查询条件
     * @return 统计结果
     */
    AuditStatsRespVO getUserAuditStats(AuditStatsReqVO reqVO);
} 