package cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.detailcheckinfo.vo.DetailCheckInfoWithDictDataRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.detailcheckinfo.DetailCheckInfoDO;
import cn.iocoder.yudao.module.system.service.detailcheckinfo.DetailCheckInfoService;
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

@Tag(name = "管理后台 - 详情检测信息")
@RestController
@RequestMapping("/wenxun/detail-check-info")
@Validated
public class DetailCheckInfoController {

    @Resource
    private DetailCheckInfoService detailCheckInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建详情检测信息")
     public CommonResult<Long> createDetailCheckInfo(@Valid @RequestBody DetailCheckInfoSaveReqVO createReqVO) {
        return success(detailCheckInfoService.createDetailCheckInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新详情检测信息")
     public CommonResult<Boolean> updateDetailCheckInfo(@Valid @RequestBody DetailCheckInfoSaveReqVO updateReqVO) {
        detailCheckInfoService.updateDetailCheckInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除详情检测信息")
    @Parameter(name = "id", description = "编号", required = true)
     public CommonResult<Boolean> deleteDetailCheckInfo(@RequestParam("id") Long id) {
        detailCheckInfoService.deleteDetailCheckInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得详情检测信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
     public CommonResult<DetailCheckInfoWithDictDataRespVO> getDetailCheckInfo(@RequestParam("id") Long id) {
        DetailCheckInfoWithDictDataRespVO detailCheckInfo = detailCheckInfoService.getDetailCheckInfo(id);
        return success(BeanUtils.toBean(detailCheckInfo, DetailCheckInfoWithDictDataRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得详情检测信息分页")
     public CommonResult<PageResult<DetailCheckInfoDO>> getDetailCheckInfoPage(@Valid DetailCheckInfoPageReqVO pageReqVO) {
        PageResult<DetailCheckInfoDO> pageResult = detailCheckInfoService.getDetailCheckInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DetailCheckInfoDO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出详情检测信息 Excel")
     @ApiAccessLog(operateType = EXPORT)
    public void exportDetailCheckInfoExcel(@Valid DetailCheckInfoPageReqVO pageReqVO,
                                           HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DetailCheckInfoDO> list = detailCheckInfoService.getDetailCheckInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "详情检测信息.xls", "数据", DetailCheckInfoWithDictDataRespVO.class,
                BeanUtils.toBean(list, DetailCheckInfoWithDictDataRespVO.class));
    }

}