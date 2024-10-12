package cn.wenxun.spider;


import cn.wenxun.admin.model.SchoolWebInfo;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class HtmlUnitExample {
    public static void main(String[] args) {
        // 创建 WebClient
        try (final WebClient webClient = new WebClient()) {
            // 禁用 JavaScript 和 CSS 支持（根据页面需求，如果需要可以启用）
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);

            // 设置等待时间
            webClient.getOptions().setTimeout(5000);

            // 加载网页
            HtmlPage page = webClient.getPage("https://www.bzpt.edu.cn");  // 替换为你需要提取的网页URL

            List<SchoolWebInfo> schoolWebInfo = new ArrayList<>();            // 获取页面标题

            String title = page.getTitleText();
            // 首页信息
            SchoolWebInfo systemInfo = new SchoolWebInfo();
            systemInfo.setModelName(title);
            systemInfo.setModelUrl(page.getUrl().getPath());
            systemInfo.setModelDesc("首页");
            systemInfo.setId("1");
            systemInfo.setWebUrl("https://www.bzpt.edu.cn");
            systemInfo.setWebName("滨州学院");
            schoolWebInfo.add(systemInfo);

            // 提取 <div> 元素中的文本
            List<HtmlDivision> divs = page.getByXPath("//div");
            List<Map> list = new ArrayList<>();
            for (HtmlDivision div : divs) {
                // 获取该 <div> 内部所有超链接
                List<Map<String, String>> alist = new ArrayList<>();
                // 获取所有 <script> 元素
                List<HtmlScript> scripts = div.getByXPath(".//script");
                // 删除所有 <script> 标签
                for (HtmlScript script : scripts) {
                    script.remove();
                }
                // 获取所有 <style> 元素并删除
                List<HtmlStyle> styles = div.getByXPath(".//style");
                for (HtmlElement style : styles) {
                    style.remove();
                }
                List<HtmlAnchor> anchors = div.getByXPath(".//a");  // 仅获取 <div> 内部的 <a> 标签
                for (HtmlAnchor anchor : anchors) {
                    String text = anchor.getTextContent();
                    String Href = anchor.getHrefAttribute();
                    if (StringUtils.isNotEmpty(Href)) {
                        text = text.replace(" ", "").replace("\n", "");
                    }
                    SchoolWebInfo aInfo = new SchoolWebInfo();
                    String url = anchor.getHrefAttribute();

                    aInfo.setModelDesc(text); // 获取链接文本
                    aInfo.setModelUrl(url);// 获取链接地址


//                    alist.add(aMap);
                }
                String text = div.getTextContent().replace(" ", "").replace("\n", "");
                if (!(CollectionUtils.isEmpty(alist) && StringUtils.isEmpty(text))) {
                    // 使用 Stream 去重并合并相同 Href 的 Text，最后返回 List<Map<String, String>>
                    alist = alist.stream()
                            .collect(Collectors.toMap(
                                    map2 -> map2.get("Href"),   // Key: Href
                                    map2 -> new HashSet<>(Collections.singletonList(map2.get("Text"))), // 用 Set 去重 Text
                                    (set1, set2) -> {
                                        set1.addAll(set2);
                                        return set1;
                                    }  // 合并 Set
                            ))
                            .entrySet().stream()
                            .map(entry -> {
                                Map<String, String> mergedMap = new HashMap<>();
                                mergedMap.put("Href", entry.getKey());
                                mergedMap.put("Text", String.join(", ", entry.getValue()));
                                return mergedMap;
                            })
                            .collect(Collectors.toList());
//                    divMap.put("HrefMap", alist);  // 提取该 <div> 的文本内容
                    for (Map<String, String> stringMap : alist) {
                        text = text.replace(stringMap.get("Text"), "");
                    }

//                    divMap.put("Div Text", text);  // 提取该 <div> 的文本内容

                }
//                if (!divMap.isEmpty()) {
//                    list.add(divMap);
//                }
            }
            // 用于存储唯一的 HrefMap 和对应的 Div Text
            Set<String> uniqueHrefSet = new HashSet<>();
            List<Map<String, Object>> uniqueList = new ArrayList<>();

            // 遍历列表
            for (Map<String, Object> divMap : list) {
                @SuppressWarnings("unchecked")
                List<Map<String, String>> hrefMapList = (List<Map<String, String>>) divMap.get("HrefMap");

                // 对 hrefMapList 中的 "Href" 进行排序并转换为唯一标识符字符串
                String hrefKey = hrefMapList.stream()
                        .map(m -> m.get("Href"))
                        .sorted() // 确保顺序无关
                        .collect(Collectors.joining(","));

                // 如果该 hrefKey 之前没有出现过，添加到结果列表中
                if (!uniqueHrefSet.contains(hrefKey)) {
                    uniqueHrefSet.add(hrefKey);
                    uniqueList.add(divMap); // 保留唯一的 map
                }
            }

//            map.put("pageDetail", uniqueList);

//            System.out.println(JSON.toJSONString(map));

            // 读取网页模块


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
