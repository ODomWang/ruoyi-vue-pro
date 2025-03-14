package cn.iocoder.yudao.module.system.controller.admin.wenxunDict;

import cn.hutool.core.io.FileUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataRespVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataSimpleRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataService;
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

@Tag(name = "管理后台 - 字典数据")
@RestController
@RequestMapping("/wenxun/dict-data")
@Validated
public class WenXunDictDataController {

    @Resource
    private WenXunDictDataService wenXunDictDataService;

    @PostMapping("/create")
    @Operation(summary = "新增字典数据")
    public CommonResult<Long> createDictData(@Valid @RequestBody WenXunDictDataSaveReqVO createReqVO) {
        Long dictDataId = wenXunDictDataService.createDictData(createReqVO);
        return success(dictDataId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改字典数据")
    public CommonResult<Boolean> updateDictData(@Valid @RequestBody WenXunDictDataSaveReqVO updateReqVO) {
        wenXunDictDataService.updateDictData(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除字典数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteDictData(Long id) {
        wenXunDictDataService.deleteDictData(id);
        return success(true);
    }

    @GetMapping(value = {"/list-all-simple", "simple-list"})
    @Operation(summary = "获得全部字典数据列表", description = "一般用于管理后台缓存字典数据在本地")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<WenXunDictDataSimpleRespVO>> getSimpleDictDataList() {
        List<WenXunDictDataDO> list = wenXunDictDataService.getDictDataList(
                CommonStatusEnum.ENABLE.getStatus(), null);

        return success(BeanUtils.toBean(list, WenXunDictDataSimpleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "/获得字典类型的分页列表")
    public CommonResult<PageResult<WenXunDictDataRespVO>> getDictTypePage(@Valid WenXunDictDataPageReqVO pageReqVO) {
        PageResult<WenXunDictDataDO> pageResult = wenXunDictDataService.getDictDataPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WenXunDictDataRespVO.class));
    }

    @GetMapping(value = "/get")
    @Operation(summary = "/查询字典数据详细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<WenXunDictDataRespVO> getDictData(@RequestParam("id") Long id) {
        WenXunDictDataDO dictData = wenXunDictDataService.getDictData(id);
        return success(BeanUtils.toBean(dictData, WenXunDictDataRespVO.class));
    }

    @GetMapping("/export")
    @Operation(summary = "导出字典数据")
    @ApiAccessLog(operateType = EXPORT)
    public void export(HttpServletResponse response, @Valid WenXunDictDataPageReqVO exportReqVO) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WenXunDictDataDO> list = wenXunDictDataService.getDictDataPage(exportReqVO).getList();
        // 输出
        ExcelUtils.write(response, "字典数据.xls", "数据", WenXunDictDataRespVO.class,
                BeanUtils.toBean(list, WenXunDictDataRespVO.class));
    }

}
