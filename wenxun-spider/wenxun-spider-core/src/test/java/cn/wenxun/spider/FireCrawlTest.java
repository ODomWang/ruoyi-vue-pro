package cn.wenxun.spider;

import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FireCrawlTest {
    public static String url = "https://www.lygsf.edu.cn/586/list.htm";
    public static Map<String, Object> headers = new HashMap<>();

    static {
        headers.put("Authorization", "Bearer fc-f6a6a5d79ee444949fbdcd47d9ede16d");
//        headers.put("Content-Type", "application/json");
    }

    public static void main(String[] args) {

//        System.out.println( map(url, headers));
//        System.out.println( crawl(url, headers));
        System.out.println(
                HttpUtil.get("https://api.firecrawl.dev/v1/crawl/78ef9e66-e350-47ab-8d4a-e969e3f7b421"
                        ,headers));

    }
    //HttpResponse<String> response = Unirest.post("https://api.firecrawl.dev/v1/crawl")
    //  .header("Authorization", "Bearer <token>")
    //  .header("Content-Type", "application/json")
    //  .body("{\n  \"url\": \"<string>\",\n  \"excludePaths\": [\n    \"<string>\"\n  ],\n  \"includePaths\": [\n    \"<string>\"\n  ],\n  \"maxDepth\": 2,\n  \"ignoreSitemap\": false,\n  \"ignoreQueryParameters\": false,\n  \"limit\": 10000,\n  \"allowBackwardLinks\": false,\n  \"allowExternalLinks\": false,\n  \"webhook\": {\n    \"url\": \"<string>\",\n    \"headers\": {},\n    \"metadata\": {},\n    \"events\": [\n      \"completed\"\n    ]\n  },\n  \"scrapeOptions\": {\n    \"formats\": [\n      \"markdown\"\n    ],\n    \"onlyMainContent\": true,\n    \"includeTags\": [\n      \"<string>\"\n    ],\n    \"excludeTags\": [\n      \"<string>\"\n    ],\n    \"headers\": {},\n    \"waitFor\": 0,\n    \"mobile\": false,\n    \"skipTlsVerification\": false,\n    \"timeout\": 30000,\n    \"jsonOptions\": {\n      \"schema\": {},\n      \"systemPrompt\": \"<string>\",\n      \"prompt\": \"<string>\"\n    },\n    \"actions\": [\n      {\n        \"type\": \"wait\",\n        \"milliseconds\": 2,\n        \"selector\": \"#my-element\"\n      }\n    ],\n    \"location\": {\n      \"country\": \"US\",\n      \"languages\": [\n        \"en-US\"\n      ]\n    },\n    \"removeBase64Images\": true,\n    \"blockAds\": true,\n    \"proxy\": \"basic\"\n  }\n}")
    //  .asString();
    public static String crawl(String url, Map<String, String> headers) {
        // 构建要发送的请求数据（JSON 格式）
        JSONObject jsonObject = new JSONObject();

        // 1. 设置 URL（目标网页的起始 URL）
        jsonObject.put("url", url);

        // 2. 设置排除的路径列表（例如，可以排除某些 URL 路径）
//        JSONArray excludePaths = new JSONArray();
//        excludePaths.add("<string>");
//        jsonObject.put("excludePaths", excludePaths);

        // 3. 设置包括的路径列表（用于指定需要爬取的路径）
//        JSONArray includePaths = new JSONArray();
//        includePaths.add("<string>");
//        jsonObject.put("includePaths", includePaths);

        // 4. 最大深度限制（控制爬取的深度，防止过度爬取）
        jsonObject.put("maxDepth", 2);

        // 5. 是否忽略 Sitemap
        jsonObject.put("ignoreSitemap", false);

        // 6. 是否忽略查询参数
        jsonObject.put("ignoreQueryParameters", false);

        // 7. 设置最大抓取数量（控制抓取的数量）
        jsonObject.put("limit", 10000);

        // 8. 设置是否允许回链
        jsonObject.put("allowBackwardLinks", false);

        // 9. 设置是否允许外部链接
        jsonObject.put("allowExternalLinks", false);

        // Webhook 配置（配置抓取完成后的回调 URL）
        JSONObject webhook = new JSONObject();
        webhook.put("url", "<string>");
        webhook.put("headers", new JSONObject()); // 空的 header 配置
        webhook.put("metadata", new JSONObject()); // 空的 metadata 配置

        // 设置 webhook 事件类型（当抓取完成时，触发回调）
        JSONArray events = new JSONArray();
        events.add("completed");
        webhook.put("events", events);

//        jsonObject.put("webhook", webhook); // 把 webhook 配置加入到 JSON 请求中

        // Scrape 配置（配置抓取页面的选项）
        JSONObject scrapeOptions = new JSONObject();

        // 10. 格式（指定抓取返回的格式，比如 markdown）
        JSONArray formats = new JSONArray();
        formats.add("markdown");
        scrapeOptions.put("formats", formats);

        // 11. 是否只抓取主要内容
        scrapeOptions.put("onlyMainContent", true);

        // 12. 包括的标签
        JSONArray includeTags = new JSONArray();
        includeTags.add("");
        scrapeOptions.put("includeTags", includeTags);

        // 13. 排除的标签
        JSONArray excludeTags = new JSONArray();
        excludeTags.add("");
        scrapeOptions.put("excludeTags", excludeTags);

        // 14. 其他配置项（例如请求头、等待时间等）
        scrapeOptions.put("headers", new JSONObject());
        scrapeOptions.put("waitFor", 0); // 等待时间
        scrapeOptions.put("mobile", false); // 是否为移动端模拟
        scrapeOptions.put("skipTlsVerification", false); // 是否跳过 TLS 验证
        scrapeOptions.put("timeout", 30000); // 请求超时设置

        // JSON 配置项（用于指定 schema 和提示文本）
        JSONObject jsonOptions = new JSONObject();
        jsonOptions.put("schema", new JSONObject());
        jsonOptions.put("systemPrompt", "提取网页中的指定内容");
        jsonOptions.put("prompt", "按照JSON返回文章的标题和内容，发布数据作者，浏览量 等数据 ");
        scrapeOptions.put("jsonOptions", jsonOptions);

        // 15. 执行动作（例如等待操作）
        JSONArray actions = new JSONArray();
        JSONObject action = new JSONObject();
        action.put("type", "wait");
        action.put("milliseconds", 2);
        action.put("selector", "#my-element");
        actions.add(action);
        scrapeOptions.put("actions", actions);

        // 16. 地理位置信息
        JSONObject location = new JSONObject();
        location.put("country", "CN");
        JSONArray languages = new JSONArray();
        languages.add("en-CN");
        location.put("languages", languages);
        scrapeOptions.put("location", location);

        // 17. 是否移除 Base64 图片
        scrapeOptions.put("removeBase64Images", true);

        // 18. 是否屏蔽广告
        scrapeOptions.put("blockAds", true);

        // 19. 设置代理类型
        scrapeOptions.put("proxy", "basic");

//        jsonObject.put("scrapeOptions", scrapeOptions);
        System.out.println(jsonObject.toJSONString());
        String resp = HttpUtils.post("https://api.firecrawl.dev/v1/crawl", headers,
                jsonObject.toJSONString());
        return resp;
    }

    public static String map(String url, Map<String, String> headers) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("url", url);
//        jsonObject.put("search", "<string>");
        jsonObject.put("ignoreSitemap", false);
        jsonObject.put("sitemapOnly", false);
        jsonObject.put("includeSubdomains", true);
        jsonObject.put("limit", 5000);
//        jsonObject.put("timeout", 123);
        String resp = HttpUtils.post("https://api.firecrawl.dev/v1/map", headers,
                jsonObject.toJSONString());
        return resp;
    }


}