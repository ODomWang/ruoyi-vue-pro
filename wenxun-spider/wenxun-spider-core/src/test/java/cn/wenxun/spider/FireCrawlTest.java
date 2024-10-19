package cn.wenxun.spider;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FireCrawlTest {
    public static void main(String[] args) {
        String url = "http://74.48.186.20:3002/v1/scrape";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer fc-f6a6a5d79ee444949fbdcd47d9ede16d");

        JSONObject json = new JSONObject();
        json.put("url", "https://www.bzpt.edu.cn/xwdr/xyyw.htm");
//         JSONObject scrapeOptions = new JSONObject();
//        scrapeOptions.put("formats", new String[]{"markdown", "html"});
        json.put("formats",new String[]{"markdown","html"});

        String response = HttpUtils.post(url, headers, json.toString());
        System.out.println("Response Body: " + response);

//
//        String crawlId = JSONObject.parseObject(response).getString("id");
//        String getUrl = "http://74.48.186.20:3002/v1/crawl/" + crawlId;
//        Map<String, String> headers2 = new HashMap<>();
//        headers2.put("Authorization", "Bearer fc-f6a6a5d79ee444949fbdcd47d9ede16d");
//
//        String getResponse = get(getUrl, headers2);
//        System.out.println("GET Response Body: " + getResponse);
//        if (JSONObject.parseObject(getResponse).containsKey("next")) {
//            String getResponseNext = get(JSONObject.parseObject(getResponse).getString("next"), headers2);
//            System.out.println("GET getResponseNext Body: " + getResponseNext);
//
//        }
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