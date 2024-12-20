package cn.iocoder.yudao.module.wenxun.controller.admin.urlchangeinfo;

import org.springframework.web.bind.annotation.*;
 import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.wenxun.controller.admin.urlchangeinfo.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.urlchangeinfo.UrlChangeInfoDO;
import cn.iocoder.yudao.module.wenxun.service.urlchangeinfo.UrlChangeInfoService;

@Tag(name = "管理后台 - 网站更新检查")
@RestController
@RequestMapping("/wenxun/url-change-info")
@Validated
public class UrlChangeInfoController {

    @Resource
    private UrlChangeInfoService urlChangeInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建网站更新检查")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-info:create')")
    public CommonResult<Integer> createUrlChangeInfo(@Valid @RequestBody UrlChangeInfoSaveReqVO createReqVO) {
        return success(urlChangeInfoService.createUrlChangeInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新网站更新检查")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-info:update')")
    public CommonResult<Boolean> updateUrlChangeInfo(@Valid @RequestBody UrlChangeInfoSaveReqVO updateReqVO) {
        urlChangeInfoService.updateUrlChangeInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除网站更新检查")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-info:delete')")
    public CommonResult<Boolean> deleteUrlChangeInfo(@RequestParam("id") Integer id) {
        urlChangeInfoService.deleteUrlChangeInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得网站更新检查")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-info:query')")
    public CommonResult<UrlChangeInfoRespVO> getUrlChangeInfo(@RequestParam("id") Integer id) {
        UrlChangeInfoDO urlChangeInfo = urlChangeInfoService.getUrlChangeInfo(id);
        return success(BeanUtils.toBean(urlChangeInfo, UrlChangeInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得网站更新检查分页")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-info:query')")
    public CommonResult<PageResult<UrlChangeInfoRespVO>> getUrlChangeInfoPage(@Valid UrlChangeInfoPageReqVO pageReqVO) {
        PageResult<UrlChangeInfoDO> pageResult = urlChangeInfoService.getUrlChangeInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UrlChangeInfoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出网站更新检查 Excel")
    @PreAuthorize("@ss.hasPermission('wenxun:url-change-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUrlChangeInfoExcel(@Valid UrlChangeInfoPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UrlChangeInfoDO> list = urlChangeInfoService.getUrlChangeInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "网站更新检查.xls", "数据", UrlChangeInfoRespVO.class,
                        BeanUtils.toBean(list, UrlChangeInfoRespVO.class));
    }

}