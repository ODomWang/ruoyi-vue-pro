package cn.wenxun.admin.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeSaveReqVO;
import cn.wenxun.admin.model.NewsInfo;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import cn.wenxun.admin.openai.SpiderAiUtils;
import cn.wenxun.admin.service.WenXunSpiderConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.net.ssl.*;
import javax.validation.Valid;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "爬虫测试接口")
@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Resource
    private WenXunSpiderConfigService wenXunSpiderConfigService;

    @PostMapping("/spidertest")
    @Operation(summary = "爬虫测试接口")
    @Parameter(name = "url", description = "url", required = true)
    public CommonResult<String> getOrder(String url) throws IOException {
        //获取用户信息
         WebClient webClient = new WebClient();
        // 关闭不需要的日志和选项
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        String url1 = "https://jou.91job.org.cn/sub-station/notificationList?xxdm=11641&lmid=4342";
        // 加载页面
        // 创建一个信任所有主机名的 HostnameVerifier
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                return true;
            }
        };

        // 创建一个信任所有证书的 TrustManager
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };

        // 安装信任管理器
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置新的 HostnameVerifier
        HttpsURLConnection.setDefaultHostnameVerifier(hv);


        HtmlPage page = webClient.getPage(url1);
        String resp = SpiderAiUtils.SpiderByOpenAi(page.asXml(), "获取网站的就业新闻列表", Collections.singleton(new NewsInfo()), "https://jou.91job.org.cn");
        return success(resp);

    }



    /*获取爬虫配置表信息
     */
    @GetMapping("/getSpiderConfigPage")
    @Operation(summary = "获取爬虫配置表信息")
    public CommonResult<PageResult<WenxunSpiderSourceConfigDO>> getSpiderConfig(@Validated PageParam pageReqVO) {
        PageResult<WenxunSpiderSourceConfigDO> list = wenXunSpiderConfigService.getDataSourceConfigList(pageReqVO);
         return  success(BeanUtils.toBean(list, WenxunSpiderSourceConfigDO.class));
     }

    /*获取爬虫配置表信息
     */
    @GetMapping("/get")
    @Operation(summary = "获取爬虫配置表信息")
    public CommonResult<WenxunSpiderSourceConfigDO> getSpiderInfo( @RequestParam("id") Long id) {
        WenxunSpiderSourceConfigDO sourceConfigDO = wenXunSpiderConfigService.getDataSourceConfig(id);
        return  success(BeanUtils.toBean(sourceConfigDO, WenxunSpiderSourceConfigDO.class));
    }
    /*获取爬虫配置表信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改爬虫配置表信息")
     public CommonResult<Boolean> updateSpiderConfig(@Valid @RequestBody WenxunSpiderSourceConfigDO sourceConfigDO) {
         wenXunSpiderConfigService.updateDataSourceConfig(sourceConfigDO);
        return  success(true);
    }



}
