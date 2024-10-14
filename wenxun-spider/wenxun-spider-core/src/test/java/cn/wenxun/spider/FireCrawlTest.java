package cn.wenxun.spider;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FireCrawlTest {
    public static void main(String[] args) {
        String url = "https://api.firecrawl.dev/v1/crawl";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer fc-f6a6a5d79ee444949fbdcd47d9ede16d");

        JSONObject json = new JSONObject();
        json.put("url", "https://jou.91job.org.cn/sub-station/notificationList?xxdm=11641&lmid=4342");
        json.put("limit", 1);
        JSONObject scrapeOptions = new JSONObject();
        scrapeOptions.put("formats", new String[]{"markdown", "html"});
        json.put("scrapeOptions", scrapeOptions);

        String response = HttpUtils.post(url, headers, json.toString());
        System.out.println("Response Body: " + response);


        String crawlId = JSONObject.parseObject(response).getString("id");
        String getUrl = "https://api.firecrawl.dev/v1/crawl/" + crawlId;
        Map<String, String> headers2 = new HashMap<>();
        headers2.put("Authorization", "Bearer fc-f6a6a5d79ee444949fbdcd47d9ede16d");

        String getResponse = get(getUrl, headers2);
        System.out.println("GET Response Body: " + getResponse);
        if (JSONObject.parseObject(getResponse).containsKey("next")) {
            String getResponseNext = get(JSONObject.parseObject(getResponse).getString("next"), headers2);
            System.out.println("GET getResponseNext Body: " + getResponseNext);

        }
    }

    public static String get(String url, Map<String, String> headers) {
        try (HttpResponse response = HttpRequest.get(url)
                .addHeaders(headers)
                .execute()) {
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}