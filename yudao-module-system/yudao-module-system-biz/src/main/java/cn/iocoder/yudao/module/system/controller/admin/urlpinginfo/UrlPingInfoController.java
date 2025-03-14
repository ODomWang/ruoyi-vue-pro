package cn.iocoder.yudao.module.system.controller.admin.urlpinginfo;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo.UrlPingInfoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo.UrlPingInfoRespVO;
import cn.iocoder.yudao.module.system.controller.admin.urlpinginfo.vo.UrlPingInfoSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlpinginfo.UrlPingInfoDO;
import cn.iocoder.yudao.module.system.service.urlpinginfo.UrlPingInfoService;
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

@Tag(name = "管理后台 - 网页连通记录")
@RestController
@RequestMapping("/wenxun/url-ping-info")
@Validated
public class UrlPingInfoController {

    @Resource
    private UrlPingInfoService urlPingInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建网页连通记录")
//    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-info:create')")
    public CommonResult<Integer> createUrlPingInfo(@Valid @RequestBody UrlPingInfoSaveReqVO createReqVO) {
        return success(urlPingInfoService.createUrlPingInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新网页连通记录")
//    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-info:update')")
    public CommonResult<Boolean> updateUrlPingInfo(@Valid @RequestBody UrlPingInfoSaveReqVO updateReqVO) {
        urlPingInfoService.updateUrlPingInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除网页连通记录")
    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-info:delete')")
    public CommonResult<Boolean> deleteUrlPingInfo(@RequestParam("id") Integer id) {
        urlPingInfoService.deleteUrlPingInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得网页连通记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-info:query')")
    public CommonResult<UrlPingInfoRespVO> getUrlPingInfo(@RequestParam("id") Integer id) {
        UrlPingInfoDO urlPingInfo = urlPingInfoService.getUrlPingInfo(id);
        return success(BeanUtils.toBean(urlPingInfo, UrlPingInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得网页连通记录分页")
//    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-info:query')")
    public CommonResult<PageResult<UrlPingInfoRespVO>> getUrlPingInfoPage(@Valid UrlPingInfoPageReqVO pageReqVO) {
        PageResult<UrlPingInfoDO> pageResult = urlPingInfoService.getUrlPingInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UrlPingInfoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出网页连通记录 Excel")
//    @PreAuthorize("@ss.hasPermission('wenxun:url-ping-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUrlPingInfoExcel(@Valid UrlPingInfoPageReqVO pageReqVO,
                                       HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UrlPingInfoDO> list = urlPingInfoService.getUrlPingInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "网页连通记录.xls", "数据", UrlPingInfoRespVO.class,
                BeanUtils.toBean(list, UrlPingInfoRespVO.class));
    }

}