package cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoPageReqVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoRespVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.detailcheckauditinfo.vo.DetailCheckAuditInfoSaveReqVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckauditinfo.DetailCheckAuditInfoDO;
import cn.iocoder.yudao.module.wenxun.service.detailcheckauditinfo.DetailCheckAuditInfoService;
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

@Tag(name = "管理后台 - 详情检测信息表-用户审核")
@RestController
@RequestMapping("/wenxun/detail-check-audit-info")
@Validated
public class DetailCheckAuditInfoController {

    @Resource
    private DetailCheckAuditInfoService detailCheckAuditInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建详情检测信息表-用户审核")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-audit-info:create')")
    public CommonResult<Long> createDetailCheckAuditInfo(@Valid @RequestBody DetailCheckAuditInfoSaveReqVO createReqVO) {
        return success(detailCheckAuditInfoService.createDetailCheckAuditInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新详情检测信息表-用户审核")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-audit-info:update')")
    public CommonResult<Boolean> updateDetailCheckAuditInfo(@Valid @RequestBody DetailCheckAuditInfoSaveReqVO updateReqVO) {
        detailCheckAuditInfoService.updateDetailCheckAuditInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除详情检测信息表-用户审核")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-audit-info:delete')")
    public CommonResult<Boolean> deleteDetailCheckAuditInfo(@RequestParam("id") Long id) {
        detailCheckAuditInfoService.deleteDetailCheckAuditInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得详情检测信息表-用户审核")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-audit-info:query')")
    public CommonResult<DetailCheckAuditInfoRespVO> getDetailCheckAuditInfo(@RequestParam("id") Long id) {
        DetailCheckAuditInfoDO detailCheckAuditInfo = detailCheckAuditInfoService.getDetailCheckAuditInfo(id);
        return success(BeanUtils.toBean(detailCheckAuditInfo, DetailCheckAuditInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得详情检测信息表-用户审核分页")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-audit-info:query')")
    public CommonResult<PageResult<DetailCheckAuditInfoRespVO>> getDetailCheckAuditInfoPage(@Valid DetailCheckAuditInfoPageReqVO pageReqVO) {
        PageResult<DetailCheckAuditInfoDO> pageResult = detailCheckAuditInfoService.getDetailCheckAuditInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DetailCheckAuditInfoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出详情检测信息表-用户审核 Excel")
    @PreAuthorize("@ss.hasPermission('wenxun:detail-check-audit-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDetailCheckAuditInfoExcel(@Valid DetailCheckAuditInfoPageReqVO pageReqVO,
                                                HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DetailCheckAuditInfoDO> list = detailCheckAuditInfoService.getDetailCheckAuditInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "详情检测信息表-用户审核.xls", "数据", DetailCheckAuditInfoRespVO.class,
                BeanUtils.toBean(list, DetailCheckAuditInfoRespVO.class));
    }

}