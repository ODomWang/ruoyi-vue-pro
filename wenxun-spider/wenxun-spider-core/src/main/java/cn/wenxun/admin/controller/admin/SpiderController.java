package cn.wenxun.admin.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.wenxun.admin.job.utils.HtmlUnitUtil;
import cn.wenxun.admin.job.utils.PlayWrightUtils;
import cn.wenxun.admin.model.NewsInfo;
import cn.wenxun.admin.model.spider.SpiderXpathConfigDO;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import cn.wenxun.admin.service.WenXunSpiderConfigService;
import cn.wenxun.admin.service.WenXunSpiderCrawlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "爬虫测试接口")
@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Resource
    private WenXunSpiderConfigService wenXunSpiderConfigService;
    @Resource
    private WenXunSpiderCrawlService wenXunSpiderCrawlService;
    @PostMapping("/spidertest")
    @Operation(summary = "爬虫测试接口")
    public CommonResult<List<NewsInfo>> getOrder(@Valid @RequestBody WenxunSpiderSourceConfigDO createReqVO) {

        List<NewsInfo> newsInfos = HtmlUnitUtil.crawlUrl(createReqVO, true);
        return success(newsInfos);

    }

    @PostMapping("/spidertest_v2")
    @Operation(summary = "爬虫测试接口")
    public CommonResult<List<NewsInfo>> getWebInfoByXpath(@Valid @RequestBody SpiderXpathConfigDO createReqVO) {
        createReqVO.setSpiderPageNum(2L);
        List<NewsInfo> newsInfos = PlayWrightUtils.crawlUrl(createReqVO,wenXunSpiderCrawlService);
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

    /*获取所有配置信息
     */
    @GetMapping("/getAllConfig")
    @Operation(summary = "获取爬虫配置表信息")
    public CommonResult<List<WenxunSpiderSourceConfigDO>> getAllSpiderConfig() {
        List<WenxunSpiderSourceConfigDO> list = wenXunSpiderConfigService.getAllUrlConfigInfo();
        return success(list);
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
    public CommonResult<Long> createConfig(@Valid @RequestBody SpiderXpathConfigDO createReqVO) {
        return success(wenXunSpiderConfigService.createDataSourceConfig(createReqVO));
    }


    @GetMapping("/proxy")
    @PermitAll
    public CommonResult<ResponseEntity<String>> proxy(@RequestParam String targetUrl) {
        try {
            // 检查目标 URL 是否有效
            URI targetUri = new URI(targetUrl);

            // 使用 RestTemplate 获取目标内容
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(targetUri, byte[].class);


            InputStream responseStream = new ByteArrayInputStream(response.getBody());
            String responseBody = getCharSet(responseStream);
            // 返回修改后的响应
            return success(ResponseEntity.ok()
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*") // 允许跨域
//                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=" + charset) // 设置内容类型
                    .body(responseBody));

        } catch (URISyntaxException e) {
            return success(ResponseEntity.badRequest().body("Invalid target URL."));
        } catch (Exception e) {
            return success(ResponseEntity.status(500).body("Failed to fetch target content: " + e.getMessage()));
        }
    }

    public static String getCharSet(InputStream in) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        byte[] buf = new byte[1024];
        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // 读取数据并检测字符集
        while ((nread = bufferedInputStream.read(buf)) > 0) {
            if (!detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            byteArrayOutputStream.write(buf, 0, nread);
        }
        detector.dataEnd();

        // 检测到的编码（默认为 UTF-8）
        String encoding = detector.getDetectedCharset();
        if (encoding == null) {
            encoding = "UTF-8";
        }

        // 将内容解码为字符串
        byte[] contents = byteArrayOutputStream.toByteArray();
        return new String(contents, encoding);
    }


}
