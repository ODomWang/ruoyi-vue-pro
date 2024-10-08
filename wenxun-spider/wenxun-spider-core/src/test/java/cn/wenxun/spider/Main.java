package cn.wenxun.spider;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try (final WebClient webClient = new WebClient()) {
            // 关闭不需要的日志和选项
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            // 加载页面
            HtmlPage page = webClient.getPage("https://www.bzpt.edu.cn");
            JSONArray divJsonArray = new JSONArray();

            // 获取页面中所有最外层的 div 元素 (没有 div 父元素的 div)
            List<HtmlDivision> outerDivs = page.getByXPath("//div[not(ancestor::div)]");

            // 遍历所有最外层的 div，并递归处理
            for (HtmlDivision div : outerDivs) {
                // 获取每个 div 的 JSON 对象，层级从 0 开始
                JSONObject divJsonObject = traverseAndBuildJson(div, 0);
                divJsonArray.add(divJsonObject);
            }
            System.out.println(JSON.toJSONString(divJsonArray));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        jsonObject.put("text", element.getTextContent().trim().replace(" ","").replace("\n",""));  // 文本内容
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