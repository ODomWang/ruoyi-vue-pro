package cn.iocoder.yudao.module.system.controller.admin.auditlog;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.auditlog.vo.AuditLogRespVO;
import cn.iocoder.yudao.module.system.controller.admin.auditlog.vo.AuditLogSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.AuditLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.auditlog.AuditLogDO;
import cn.iocoder.yudao.module.system.service.auditlog.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 人工研判审核")
@RestController
@RequestMapping("/wenxun/audit-log")
@Validated
public class AuditLogController {

    @Resource
    private AuditLogService auditLogService;

    @PostMapping("/create")
    @Operation(summary = "创建人工研判审核")
    public CommonResult<Integer> createAuditLog(@Valid @RequestBody AuditLogSaveReqVO createReqVO) {
        return success(auditLogService.createAuditLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新人工研判审核")
    public CommonResult<Boolean> updateAuditLog(@Valid @RequestBody AuditLogSaveReqVO updateReqVO) {
        auditLogService.updateAuditLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除人工研判审核")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteAuditLog(@RequestParam("id") Integer id) {
        auditLogService.deleteAuditLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得人工研判审核")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AuditLogRespVO> getAuditLog(@RequestParam("id") Integer id) {
        AuditLogDO auditLog = auditLogService.getAuditLog(id);
        return success(BeanUtils.toBean(auditLog, AuditLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得人工研判审核分页")
    public CommonResult<PageResult<AuditLogRespVO>> getAuditLogPage(@Valid AuditLogPageReqVO pageReqVO) {
        PageResult<AuditLogDO> pageResult = auditLogService.getAuditLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AuditLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出人工研判审核 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAuditLogExcel(@Valid AuditLogPageReqVO pageReqVO,
                                    HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AuditLogDO> list = auditLogService.getAuditLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "人工研判审核.xls", "数据", AuditLogRespVO.class,
                BeanUtils.toBean(list, AuditLogRespVO.class));
    }

}