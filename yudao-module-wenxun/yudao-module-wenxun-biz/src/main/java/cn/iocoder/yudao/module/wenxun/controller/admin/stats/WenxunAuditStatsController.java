package cn.iocoder.yudao.module.wenxun.controller.admin.stats;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.AuditStatsReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.stats.vo.AuditStatsRespVO;
import cn.iocoder.yudao.module.wenxun.service.stats.WenxunAuditStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 文巡审核统计")
@RestController
@RequestMapping("/wenxun/stats/audit")
@Validated
public class WenxunAuditStatsController {
    
    @Resource
    private WenxunAuditStatsService auditStatsService;
    
    @Resource
    private PermissionApi permissionApi;
    
    @PostMapping("")
    @Operation(summary = "获取审核统计数据")
    public CommonResult<AuditStatsRespVO> getAuditStats(@Validated @RequestBody AuditStatsReqVO reqVO) {
        // 判断用户是否有管理员权限
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        boolean isAdmin = permissionApi.hasAnyRoles(userId, new String[]{"super-admin", "tenant-admin"});
        
        // 管理员可以查看全部数据，也可选择只查看自己的数据
        if (isAdmin) {
            // 如果管理员指定只查看自己的数据
            if (Boolean.TRUE.equals(reqVO.getSelfOnly())) {
                return success(auditStatsService.getUserAuditStats(reqVO));
            } else {
                // 管理员默认查看所有数据
                return success(auditStatsService.getAuditStats(reqVO));
            }
        } else {
            // 普通用户只能查看自己的数据，强制设置为selfOnly=true
            reqVO.setSelfOnly(true);
            return success(auditStatsService.getUserAuditStats(reqVO));
        }
    }
} 