package cn.iocoder.yudao.module.system.controller.admin.wenxunDict;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypeRespVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypeSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.type.WenXunDictTypeSimpleRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictTypeDO;
import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictTypeService;
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

@Tag(name = "管理后台 - 字典类型")
@RestController
@RequestMapping("/wenxun/dict-type")
@Validated
public class WenXunDictTypeController {

    @Resource
    private WenXunDictTypeService wenXunDictTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建字典类型")
    @PreAuthorize("@ss.hasPermission('wenxun:dict:create')")
    public CommonResult<Long> createDictType(@Valid @RequestBody WenXunDictTypeSaveReqVO createReqVO) {
        Long dictTypeId = wenXunDictTypeService.createDictType(createReqVO);
        return success(dictTypeId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改字典类型")
    @PreAuthorize("@ss.hasPermission('wenxun:dict:update')")
    public CommonResult<Boolean> updateDictType(@Valid @RequestBody WenXunDictTypeSaveReqVO updateReqVO) {
        wenXunDictTypeService.updateDictType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除字典类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wenxun:dict:delete')")
    public CommonResult<Boolean> deleteDictType(Long id) {
        wenXunDictTypeService.deleteDictType(id);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得字典类型的分页列表")
    @PreAuthorize("@ss.hasPermission('wenxun:dict:query')")
    public CommonResult<PageResult<WenXunDictTypeRespVO>> pageDictTypes(@Valid WenXunDictTypePageReqVO pageReqVO) {
        PageResult<WenXunDictTypeDO> pageResult = wenXunDictTypeService.getDictTypePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WenXunDictTypeRespVO.class));
    }

    @Operation(summary = "/查询字典类型详细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @GetMapping(value = "/get")
    @PreAuthorize("@ss.hasPermission('wenxun:dict:query')")
    public CommonResult<WenXunDictTypeRespVO> getDictType(@RequestParam("id") Long id) {
        WenXunDictTypeDO dictType = wenXunDictTypeService.getDictType(id);
        return success(BeanUtils.toBean(dictType, WenXunDictTypeRespVO.class));
    }

    @GetMapping(value = {"/list-all-simple", "simple-list"})
    @Operation(summary = "获得全部字典类型列表", description = "包括开启 + 禁用的字典类型，主要用于前端的下拉选项")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<WenXunDictTypeSimpleRespVO>> getSimpleDictTypeList() {
        List<WenXunDictTypeDO> list = wenXunDictTypeService.getDictTypeList();
        return success(BeanUtils.toBean(list, WenXunDictTypeSimpleRespVO.class));
    }

    @Operation(summary = "导出数据类型")
    @GetMapping("/export")
    @PreAuthorize("@ss.hasPermission('wenxun:dict:query')")
    @ApiAccessLog(operateType = EXPORT)
    public void export(HttpServletResponse response, @Valid WenXunDictTypePageReqVO exportReqVO) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WenXunDictTypeDO> list = wenXunDictTypeService.getDictTypePage(exportReqVO).getList();
        // 导出
        ExcelUtils.write(response, "字典类型.xls", "数据", WenXunDictTypeRespVO.class,
                BeanUtils.toBean(list, WenXunDictTypeRespVO.class));
    }

}
