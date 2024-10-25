package cn.wenxun.admin.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.wenxun.admin.job.utils.HtmlUnitUtil;
import cn.wenxun.admin.model.NewsInfo;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import cn.wenxun.admin.service.WenXunSpiderConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "爬虫测试接口")
@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Resource
    private WenXunSpiderConfigService wenXunSpiderConfigService;

    @PostMapping("/spidertest")
    @Operation(summary = "爬虫测试接口")
    public CommonResult<List<NewsInfo>> getOrder(@Valid @RequestBody WenxunSpiderSourceConfigDO createReqVO) {

        List<NewsInfo> newsInfos = HtmlUnitUtil.crawlUrl(createReqVO, true);
        return success(newsInfos);

    }


    /*获取爬虫配置表信息
     */
    @GetMapping("/getSpiderConfigPage")
    @Operation(summary = "获取爬虫配置表信息")
    public CommonResult<PageResult<WenxunSpiderSourceConfigDO>> getSpiderConfig(@Validated PageParam pageReqVO) {
        PageResult<WenxunSpiderSourceConfigDO> list = wenXunSpiderConfigService.getDataSourceConfigList(pageReqVO);
        return success(BeanUtils.toBean(list, WenxunSpiderSourceConfigDO.class));
    }

    /*获取爬虫配置表信息
     */
    @GetMapping("/get")
    @Operation(summary = "获取爬虫配置表信息")
    public CommonResult<WenxunSpiderSourceConfigDO> getSpiderInfo(@RequestParam("id") Long id) {
        WenxunSpiderSourceConfigDO sourceConfigDO = wenXunSpiderConfigService.getDataSourceConfig(id);
        return success(BeanUtils.toBean(sourceConfigDO, WenxunSpiderSourceConfigDO.class));
    }

    /*获取爬虫配置表信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改爬虫配置表信息")
    public CommonResult<Boolean> updateSpiderConfig(@Valid @RequestBody WenxunSpiderSourceConfigDO sourceConfigDO) {
        wenXunSpiderConfigService.updateDataSourceConfig(sourceConfigDO);
        return success(true);
    }


    @PostMapping("/create")
    @Operation(summary = "创建参数配置")
//    @PreAuthorize("@ss.hasPermission('infra:config:create')")
    public CommonResult<Long> createConfig(@Valid @RequestBody WenxunSpiderSourceConfigDO createReqVO) {
        return success(wenXunSpiderConfigService.createDataSourceConfig(createReqVO));
    }

}
