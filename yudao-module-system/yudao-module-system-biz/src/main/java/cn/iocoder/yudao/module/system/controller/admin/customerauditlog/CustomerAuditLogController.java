package cn.iocoder.yudao.module.system.controller.admin.customerauditlog;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.customerauditlog.vo.CustomerAuditLogPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.customerauditlog.vo.CustomerAuditLogRespVO;
import cn.iocoder.yudao.module.system.controller.admin.customerauditlog.vo.CustomerAuditLogSaveReqVO;
import cn.iocoder.yudao.module.system.service.customerauditlog.CustomerAuditLogService;
import cn.iocoder.yudao.module.system.dal.dataobject.customerauditlog.CustomerAuditLogDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 客户审核")
@RestController
@RequestMapping("/wenxun/customer-audit-log")
@Validated
public class CustomerAuditLogController {

    @Resource
    private CustomerAuditLogService customerAuditLogService;

    @PostMapping("/create")
    @Operation(summary = "创建客户审核")
    public CommonResult<Integer> createCustomerAuditLog(@Valid @RequestBody CustomerAuditLogSaveReqVO createReqVO) {
        return success(customerAuditLogService.createCustomerAuditLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户审核")
    public CommonResult<Boolean> updateCustomerAuditLog(@Valid @RequestBody CustomerAuditLogSaveReqVO updateReqVO) {
        customerAuditLogService.updateCustomerAuditLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除客户审核")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCustomerAuditLog(@RequestParam("id") Integer id) {
        customerAuditLogService.deleteCustomerAuditLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得客户审核")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<CustomerAuditLogRespVO> getCustomerAuditLog(@RequestParam("id") Integer id) {
        CustomerAuditLogDO customerAuditLog = customerAuditLogService.getCustomerAuditLog(id);
        return success(BeanUtils.toBean(customerAuditLog, CustomerAuditLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得客户审核分页")
    public CommonResult<PageResult<CustomerAuditLogRespVO>> getCustomerAuditLogPage(@Valid CustomerAuditLogPageReqVO pageReqVO) {
        PageResult<CustomerAuditLogDO> pageResult = customerAuditLogService.getCustomerAuditLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CustomerAuditLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出客户审核 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCustomerAuditLogExcel(@Valid CustomerAuditLogPageReqVO pageReqVO,
                                            HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CustomerAuditLogDO> list = customerAuditLogService.getCustomerAuditLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "客户审核.xls", "数据", CustomerAuditLogRespVO.class,
                BeanUtils.toBean(list, CustomerAuditLogRespVO.class));
    }

}