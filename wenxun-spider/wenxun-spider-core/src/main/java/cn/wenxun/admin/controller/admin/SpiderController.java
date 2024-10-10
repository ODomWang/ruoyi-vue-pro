package cn.wenxun.admin.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.wenxun.admin.controller.model.NewsInfo;
import cn.wenxun.admin.controller.openai.SpiderAiUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "爬虫测试接口")
@RestController
@RequestMapping("/spider")
public class SpiderController {


    @PostMapping("/spidertest")
    @Operation(summary = "爬虫测试接口")
    @Parameter(name = "url", description = "url", required = true)
    public CommonResult<JSONArray> getOrder(String url) throws IOException {

        WebClient webClient = new WebClient();
        // 关闭不需要的日志和选项
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        String url1 = "https://www.bzpt.edu.cn/xwdr/xyyw.htm";
        // 加载页面
        HtmlPage page = webClient.getPage(url1);
        String resp = SpiderAiUtils.SpiderByOpenAi(page.asXml(), "获取网站的校园要闻列表", Collections.singleton(new NewsInfo()), "https://www.bzpt.edu.cn");
        JSONArray divJsonArray = new JSONArray();

        // 获取页面中所有最外层的 div 元素 (没有 div 父元素的 div)
        List<HtmlDivision> outerDivs = page.getByXPath("//div[not(ancestor::div)]");

        // 遍历所有最外层的 div，并递归处理
        for (HtmlDivision div : outerDivs) {
            // 获取每个 div 的 JSON 对象，层级从 0 开始
            JSONObject divJsonObject = traverseAndBuildJson(div, 0);
            divJsonArray.add(divJsonObject);
        }
        return success(divJsonArray);

//            return (JSON.toJSONString(divJsonArray));


    }

    // 递归方法：逐级遍历元素并构建 JSON 对象
    private static JSONObject traverseAndBuildJson(DomElement element, int level) {
        // 获取所有 <script> 元素
        List<HtmlScript> scripts = element.getByXPath(".//script");
        // 删除所有 <script> 标签
        for (HtmlScript script : scripts) {
            script.remove();
        }
        // 获取所有 <style> 元素并删除
        List<HtmlStyle> styles = element.getByXPath(".//style");
        for (HtmlElement style : styles) {
            style.remove();
        }
        // 构建当前元素的 JSON 对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tag", element.getTagName());  // 标签名称
        jsonObject.put("text", element.getTextContent().trim().replace(" ", "").replace("\n", ""));  // 文本内容
        jsonObject.put("level", level);  // 当前元素的层级

        // 如果是 <a> 标签，添加 href 属性
        if ("a".equalsIgnoreCase(element.getTagName())) {
            jsonObject.put("href", element.getAttribute("href"));
        }

        // 递归处理子元素
        JSONArray childrenArray = new JSONArray();
        for (DomElement childElement : element.getChildElements()) {
            // 递归调用子元素并将其添加到子元素数组
            JSONObject childJson = traverseAndBuildJson(childElement, level + 1);
            childrenArray.add(childJson);
        }

        // 如果有子元素，将其添加到 JSON 对象中
        if (childrenArray.size() > 0) {
            jsonObject.put("children", childrenArray);
        }

        return jsonObject;
    }
}
