package cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo;

import org.springframework.web.bind.annotation.*;
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

import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckinfo.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import cn.iocoder.yudao.module.wenxun.service.detailcheckinfo.DetailCheckInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 详情检测信息")
@RestController
@RequestMapping("/wenxun/detail-check-info")
@Validated
public class DetailCheckInfoController {

    @Resource
    private DetailCheckInfoService detailCheckInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建详情检测信息")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-info:create')")
    public CommonResult<Long> createDetailCheckInfo(@Valid @RequestBody DetailCheckInfoSaveReqVO createReqVO) {
        return success(detailCheckInfoService.createDetailCheckInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新详情检测信息")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-info:update')")
    public CommonResult<Boolean> updateDetailCheckInfo(@Valid @RequestBody DetailCheckInfoSaveReqVO updateReqVO) {
        detailCheckInfoService.updateDetailCheckInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除详情检测信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-info:delete')")
    public CommonResult<Boolean> deleteDetailCheckInfo(@RequestParam("id") Long id) {
        detailCheckInfoService.deleteDetailCheckInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得详情检测信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-info:query')")
    public CommonResult<DetailCheckInfoRespVO> getDetailCheckInfo(@RequestParam("id") Long id) {
        DetailCheckInfoDO detailCheckInfo = detailCheckInfoService.getDetailCheckInfo(id);
        return success(BeanUtils.toBean(detailCheckInfo, DetailCheckInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得详情检测信息分页")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-info:query')")
    public CommonResult<PageResult<DetailCheckInfoRespVO>> getDetailCheckInfoPage(@Valid DetailCheckInfoPageReqVO pageReqVO) {
        PageResult<DetailCheckInfoDO> pageResult = detailCheckInfoService.getDetailCheckInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DetailCheckInfoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出详情检测信息 Excel")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDetailCheckInfoExcel(@Valid DetailCheckInfoPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DetailCheckInfoDO> list = detailCheckInfoService.getDetailCheckInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "详情检测信息.xls", "数据", DetailCheckInfoRespVO.class,
                        BeanUtils.toBean(list, DetailCheckInfoRespVO.class));
    }

}