package cn.iocoder.yudao.module.wenxun.controller.admin.urlpinglog;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.wenxun.controller.admin.urlpinglog.vo.UrlPingLogPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.urlpinglog.vo.UrlPingLogRespVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.urlpinglog.vo.UrlPingLogSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlpinglog.UrlPingLogDO;
import cn.iocoder.yudao.module.wenxun.service.urlpinglog.UrlPingLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 网页连通记录-日志")
@RestController
@RequestMapping("/wenxun/url-ping-log")
@Validated
public class UrlPingLogController {

    @Resource
    private UrlPingLogService urlPingLogService;

    @PostMapping("/create")
    @Operation(summary = "创建网页连通记录-日志")
    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-log:create')")
    public CommonResult<Integer> createUrlPingLog(@Valid @RequestBody UrlPingLogSaveReqVO createReqVO) {
        return success(urlPingLogService.createUrlPingLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新网页连通记录-日志")
    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-log:update')")
    public CommonResult<Boolean> updateUrlPingLog(@Valid @RequestBody UrlPingLogSaveReqVO updateReqVO) {
        urlPingLogService.updateUrlPingLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除网页连通记录-日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-log:delete')")
    public CommonResult<Boolean> deleteUrlPingLog(@RequestParam("id") Integer id) {
        urlPingLogService.deleteUrlPingLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得网页连通记录-日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-log:query')")
    public CommonResult<UrlPingLogRespVO> getUrlPingLog(@RequestParam("id") Integer id) {
        UrlPingLogDO urlPingLog = urlPingLogService.getUrlPingLog(id);
        return success(BeanUtils.toBean(urlPingLog, UrlPingLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得网页连通记录-日志分页")
    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-log:query')")
    public CommonResult<PageResult<UrlPingLogRespVO>> getUrlPingLogPage(@Valid UrlPingLogPageReqVO pageReqVO) {
        PageResult<UrlPingLogDO> pageResult = urlPingLogService.getUrlPingLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UrlPingLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出网页连通记录-日志 Excel")
    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-log:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUrlPingLogExcel(@Valid UrlPingLogPageReqVO pageReqVO,
                                      HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UrlPingLogDO> list = urlPingLogService.getUrlPingLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "网页连通记录-日志.xls", "数据", UrlPingLogRespVO.class,
                BeanUtils.toBean(list, UrlPingLogRespVO.class));
    }

}