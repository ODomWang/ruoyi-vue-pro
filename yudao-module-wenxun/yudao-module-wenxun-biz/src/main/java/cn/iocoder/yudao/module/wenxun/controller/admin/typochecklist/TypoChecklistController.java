package cn.iocoder.yudao.module.wenxun.controller.admin.typochecklist;

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

import cn.iocoder.yudao.module.wenxun.controller.admin.typochecklist.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.typochecklist.TypoChecklistDO;
import cn.iocoder.yudao.module.wenxun.service.typochecklist.TypoChecklistService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 错词检查")
@RestController
@RequestMapping("/wenxun/typo-checklist")
@Validated
public class TypoChecklistController {

    @Resource
    private TypoChecklistService typoChecklistService;

    @PostMapping("/create")
    @Operation(summary = "创建错词检查")
    @PreAuthorize("@ss.hasPermission('wenxun:typo-checklist:create')")
    public CommonResult<Long> createTypoChecklist(@Valid @RequestBody TypoChecklistSaveReqVO createReqVO) {
        return success(typoChecklistService.createTypoChecklist(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新错词检查")
    @PreAuthorize("@ss.hasPermission('wenxun:typo-checklist:update')")
    public CommonResult<Boolean> updateTypoChecklist(@Valid @RequestBody TypoChecklistSaveReqVO updateReqVO) {
        typoChecklistService.updateTypoChecklist(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除错词检查")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wenxun:typo-checklist:delete')")
    public CommonResult<Boolean> deleteTypoChecklist(@RequestParam("id") Long id) {
        typoChecklistService.deleteTypoChecklist(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得错词检查")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wenxun:typo-checklist:query')")
    public CommonResult<TypoChecklistRespVO> getTypoChecklist(@RequestParam("id") Long id) {
        TypoChecklistDO typoChecklist = typoChecklistService.getTypoChecklist(id);
        return success(BeanUtils.toBean(typoChecklist, TypoChecklistRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得错词检查分页")
    @PreAuthorize("@ss.hasPermission('wenxun:typo-checklist:query')")
    public CommonResult<PageResult<TypoChecklistRespVO>> getTypoChecklistPage(@Valid TypoChecklistPageReqVO pageReqVO) {
        PageResult<TypoChecklistDO> pageResult = typoChecklistService.getTypoChecklistPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TypoChecklistRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出错词检查 Excel")
    @PreAuthorize("@ss.hasPermission('wenxun:typo-checklist:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportTypoChecklistExcel(@Valid TypoChecklistPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TypoChecklistDO> list = typoChecklistService.getTypoChecklistPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "错词检查.xls", "数据", TypoChecklistRespVO.class,
                        BeanUtils.toBean(list, TypoChecklistRespVO.class));
    }

}