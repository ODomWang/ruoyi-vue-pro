package cn.iocoder.yudao.module.system.controller.admin.typochecklist;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.typochecklist.vo.TypoChecklistPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.typochecklist.vo.TypoChecklistRespVO;
import cn.iocoder.yudao.module.system.controller.admin.typochecklist.vo.TypoChecklistSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.typochecklist.TypoChecklistDO;
import cn.iocoder.yudao.module.system.service.typochecklist.TypoChecklistService;
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

@Tag(name = "管理后台 - 错词检查")
@RestController
@RequestMapping("/wenxun/typo-checklist")
@Validated
public class TypoChecklistController {

    @Resource
    private TypoChecklistService typoChecklistService;

    @PostMapping("/create")
    @Operation(summary = "创建错词检查")
    public CommonResult<Long> createTypoChecklist(@Valid @RequestBody TypoChecklistSaveReqVO createReqVO) {
        return success(typoChecklistService.createTypoChecklist(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新错词检查")
    public CommonResult<Boolean> updateTypoChecklist(@Valid @RequestBody TypoChecklistSaveReqVO updateReqVO) {
        typoChecklistService.updateTypoChecklist(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除错词检查")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteTypoChecklist(@RequestParam("id") Long id) {
        typoChecklistService.deleteTypoChecklist(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得错词检查")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<TypoChecklistRespVO> getTypoChecklist(@RequestParam("id") Long id) {
        TypoChecklistDO typoChecklist = typoChecklistService.getTypoChecklist(id);
        return success(BeanUtils.toBean(typoChecklist, TypoChecklistRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得错词检查分页")
    public CommonResult<PageResult<TypoChecklistRespVO>> getTypoChecklistPage(@Valid TypoChecklistPageReqVO pageReqVO) {
        PageResult<TypoChecklistDO> pageResult = typoChecklistService.getTypoChecklistPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TypoChecklistRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出错词检查 Excel")
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