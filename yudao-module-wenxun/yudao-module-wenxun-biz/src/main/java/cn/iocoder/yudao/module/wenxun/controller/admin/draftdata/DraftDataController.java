package cn.iocoder.yudao.module.wenxun.controller.admin.draftdata;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo.DraftDataPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo.DraftDataRespVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.draftdata.vo.DraftDataSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.draftdata.DraftDataDO;
import cn.iocoder.yudao.module.wenxun.service.draftdata.DraftDataService;
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

@Tag(name = "管理后台 - 文巡-在线检测草稿")
@RestController
@RequestMapping("/wenxun/draft-data")
@Validated
public class DraftDataController {

    @Resource
    private DraftDataService draftDataService;

    @PostMapping("/create")
    @Operation(summary = "创建文巡-在线检测草稿")
    @PreAuthorize("@ss.hasPermission('wenxun:draft-data:create')")
    public CommonResult<Long> createDraftData(@Valid @RequestBody DraftDataSaveReqVO createReqVO) {
        return success(draftDataService.createDraftData(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文巡-在线检测草稿")
    @PreAuthorize("@ss.hasPermission('wenxun:draft-data:update')")
    public CommonResult<Boolean> updateDraftData(@Valid @RequestBody DraftDataSaveReqVO updateReqVO) {
        draftDataService.updateDraftData(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文巡-在线检测草稿")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wenxun:draft-data:delete')")
    public CommonResult<Boolean> deleteDraftData(@RequestParam("id") Long id) {
        draftDataService.deleteDraftData(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文巡-在线检测草稿")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wenxun:draft-data:query')")
    public CommonResult<DraftDataRespVO> getDraftData(@RequestParam("id") Long id) {
        DraftDataDO draftData = draftDataService.getDraftData(id);
        return success(BeanUtils.toBean(draftData, DraftDataRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文巡-在线检测草稿分页")
    @PreAuthorize("@ss.hasPermission('wenxun:draft-data:query')")
    public CommonResult<PageResult<DraftDataRespVO>> getDraftDataPage(@Valid DraftDataPageReqVO pageReqVO) {
        PageResult<DraftDataDO> pageResult = draftDataService.getDraftDataPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DraftDataRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出文巡-在线检测草稿 Excel")
    @PreAuthorize("@ss.hasPermission('wenxun:draft-data:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDraftDataExcel(@Valid DraftDataPageReqVO pageReqVO,
                                     HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DraftDataDO> list = draftDataService.getDraftDataPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "文巡-在线检测草稿.xls", "数据", DraftDataRespVO.class,
                BeanUtils.toBean(list, DraftDataRespVO.class));
    }

}