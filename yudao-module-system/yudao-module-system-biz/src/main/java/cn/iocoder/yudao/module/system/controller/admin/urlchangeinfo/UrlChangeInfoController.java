package cn.iocoder.yudao.module.system.controller.admin.urlchangeinfo;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.urlchangeinfo.vo.UrlChangeInfoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.urlchangeinfo.vo.UrlChangeInfoRespVO;
import cn.iocoder.yudao.module.system.controller.admin.urlchangeinfo.vo.UrlChangeInfoSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.urlchangeinfo.UrlChangeInfoDO;
import cn.iocoder.yudao.module.system.service.urlchangeinfo.UrlChangeInfoService;
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

@Tag(name = "管理后台 - 网站更新检查")
@RestController
@RequestMapping("/wenxun/url-change-info")
@Validated
public class UrlChangeInfoController {

    @Resource
    private UrlChangeInfoService urlChangeInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建网站更新检查")
    public CommonResult<Integer> createUrlChangeInfo(@Valid @RequestBody UrlChangeInfoSaveReqVO createReqVO) {
        return success(urlChangeInfoService.createUrlChangeInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新网站更新检查")
    public CommonResult<Boolean> updateUrlChangeInfo(@Valid @RequestBody UrlChangeInfoSaveReqVO updateReqVO) {
        urlChangeInfoService.updateUrlChangeInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除网站更新检查")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteUrlChangeInfo(@RequestParam("id") Integer id) {
        urlChangeInfoService.deleteUrlChangeInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得网站更新检查")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<UrlChangeInfoRespVO> getUrlChangeInfo(@RequestParam("id") Integer id) {
        UrlChangeInfoDO urlChangeInfo = urlChangeInfoService.getUrlChangeInfo(id);
        return success(BeanUtils.toBean(urlChangeInfo, UrlChangeInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得网站更新检查分页")
    public CommonResult<PageResult<UrlChangeInfoRespVO>> getUrlChangeInfoPage(@Valid UrlChangeInfoPageReqVO pageReqVO) {
        PageResult<UrlChangeInfoDO> pageResult = urlChangeInfoService.getUrlChangeInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UrlChangeInfoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出网站更新检查 Excel")
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