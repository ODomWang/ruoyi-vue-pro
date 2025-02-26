package cn.iocoder.yudao.module.wenxun.controller.admin.urlchangelog;

import org.springframework.web.bind.annotation.*;
 import jakarta.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;
import jakarta.annotation.Resource;

import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.wenxun.controller.admin.urlchangelog.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlchangelog.UrlChangeLogDO;
import cn.iocoder.yudao.module.wenxun.service.urlchangelog.UrlChangeLogService;

@Tag(name = "管理后台 - 网站更新检查-日志")
@RestController
@RequestMapping("/wenxun/url-change-log")
@Validated
public class UrlChangeLogController {

    @Resource
    private UrlChangeLogService urlChangeLogService;

    @PostMapping("/create")
    @Operation(summary = "创建网站更新检查-日志")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-log:create')")
    public CommonResult<Integer> createUrlChangeLog(@Valid @RequestBody UrlChangeLogSaveReqVO createReqVO) {
        return success(urlChangeLogService.createUrlChangeLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新网站更新检查-日志")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-log:update')")
    public CommonResult<Boolean> updateUrlChangeLog(@Valid @RequestBody UrlChangeLogSaveReqVO updateReqVO) {
        urlChangeLogService.updateUrlChangeLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除网站更新检查-日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-log:delete')")
    public CommonResult<Boolean> deleteUrlChangeLog(@RequestParam("id") Integer id) {
        urlChangeLogService.deleteUrlChangeLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得网站更新检查-日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-log:query')")
    public CommonResult<UrlChangeLogRespVO> getUrlChangeLog(@RequestParam("id") Integer id) {
        UrlChangeLogDO urlChangeLog = urlChangeLogService.getUrlChangeLog(id);
        return success(BeanUtils.toBean(urlChangeLog, UrlChangeLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得网站更新检查-日志分页")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-log:query')")
    public CommonResult<PageResult<UrlChangeLogRespVO>> getUrlChangeLogPage(@Valid UrlChangeLogPageReqVO pageReqVO) {
        PageResult<UrlChangeLogDO> pageResult = urlChangeLogService.getUrlChangeLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UrlChangeLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出网站更新检查-日志 Excel")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-log:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUrlChangeLogExcel(@Valid UrlChangeLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UrlChangeLogDO> list = urlChangeLogService.getUrlChangeLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "网站更新检查-日志.xls", "数据", UrlChangeLogRespVO.class,
                        BeanUtils.toBean(list, UrlChangeLogRespVO.class));
    }

}