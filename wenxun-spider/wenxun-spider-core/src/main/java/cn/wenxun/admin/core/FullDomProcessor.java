package cn.wenxun.admin.core;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullDomProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setSleepTime(1000);

    @Override
    public void process(Page page) {
        // 从根元素开始递归解析整个 DOM 树
        List<Map<String, Object>> elements = parseElement(page.getHtml());

        // 将解析结果保存到 Page 中
        page.putField("elements", elements);

        // 输出调试信息
        for (Map<String, Object> element : elements) {
            printElement(element, 0);
        }
    }

    // 递归解析每个 HTML 元素及其子元素
    private List<Map<String, Object>> parseElement(Selectable root) {
        List<Selectable> children = root.nodes(); // 获取当前层所有子元素
        List<Map<String, Object>> elementList = new ArrayList<>();

        // 遍历当前层的所有元素
        for (int i = 0; i < children.size(); i++) {
            Selectable child = children.get(i);
            Map<String, Object> elementData = new HashMap<>();

            // 获取当前元素的标签名（使用正则表达式提取）
            String rawHtml = child.get();
            String tagName = getTagNameFromHtml(rawHtml);
            elementData.put("tag", tagName);

            // 获取当前元素的文本内容
            String text = child.xpath("text()").get();  // 使用 text() 提取纯文本内容
            elementData.put("text", text != null ? text.trim() : "");

            // 获取当前元素的所有属性（使用正则表达式提取）
            Map<String, String> attributes = getAttributesFromHtml(rawHtml);
            elementData.put("attributes", attributes);

            // 递归获取子元素
            List<Map<String, Object>> subElements = parseElement(child.xpath("//")); // 递归获取子元素
            elementData.put("children", subElements);

            // 将当前元素添加到列表中
            elementList.add(elementData);
        }

        return elementList;
    }

    // 使用正则表达式从 HTML 片段中提取标签名
    private String getTagNameFromHtml(String html) {
        Pattern pattern = Pattern.compile("<\\s*(\\w+)");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);  // 返回标签名
        }
        return "unknown";  // 无法匹配时返回 unknown
    }

    // 使用正则表达式从 HTML 片段中提取所有属性
    private Map<String, String> getAttributesFromHtml(String html) {
        Map<String, String> attributes = new HashMap<>();
        try {
            // 提取形如 `key="value"` 的属性
            Pattern pattern = Pattern.compile("(\\w+)\\s*=\\s*\"([^\"]*?)\"");
            Matcher matcher = pattern.matcher(html);
            while (matcher.find()) {
                attributes.put(matcher.group(1), matcher.group(2));
            }
        } catch (StackOverflowError e) {
            System.err.println("正则表达式解析出错: " + e.getMessage());
        }
        return attributes;
    }

    // 打印解析后的元素及其子元素
    private void printElement(Map<String, Object> element, int indent) {
        String indentStr = " ".repeat(indent * 2);
        System.out.println(indentStr + "标签: " + element.get("tag"));
        System.out.println(indentStr + "文本: " + element.get("text"));
        System.out.println(indentStr + "属性: " + element.get("attributes"));

        List<Map<String, Object>> children = (List<Map<String, Object>>) element.get("children");
        if (children != null && !children.isEmpty()) {
            System.out.println(indentStr + "子元素:");
            for (Map<String, Object> child : children) {
                printElement(child, indent + 1);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        // 启动爬虫
        Spider.create(new FullDomProcessor())
                .addUrl("https://www.bzpt.edu.cn")  // 替换为实际的要爬取的网页
                .thread(1)
                .run();
    }
}
