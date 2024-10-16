package cn.wenxun.spider;


import cn.wenxun.admin.job.utils.PageExtracUtils;
import cn.wenxun.admin.model.NewsInfo;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlUnitExample {
    public static void main(String[] args) {
        // 创建 WebClient
        try (final WebClient webClient = new WebClient()) {
            // 禁用 JavaScript 和 CSS 支持（根据页面需求，如果需要可以启用）
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(true);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setDoNotTrackEnabled(false);

            // 设置等待时间
            webClient.getOptions().setTimeout(5000);

            // 加载网页
            HtmlPage page = webClient.getPage("https://www.bzpt.edu.cn/xwdr/xyyw/232.htm");  // 滨州职业学院
//            HtmlPage page = webClient.getPage("https://www.suda.edu.cn/suda_news/sdyw/index.html");  // 苏州大学
//            HtmlPage page = webClient.getPage("http://www.wxcu.edu.cn/static/newList.html?cid=19");  // 无锡城市学院
            webClient.waitForBackgroundJavaScript(10000); // 等待 JavaScript 执行完成，最多等待 10 秒

            if (page.asXml().contains("window.onload();")) {
                page.executeJavaScript("if (window.onload) window.onload();");
            }
            String nextPageUrl = PageExtracUtils.getNextPageUrl(page,"");
            List<HtmlElement> lists = page.getByXPath("//*[@id=\"app\"]/div[4]/div/ul");

            List<NewsInfo> lists1 = new ArrayList<>();
            for (HtmlElement list : lists) {
                // 判断是 <ul> 还是 <table>
                if ("ul".equals(list.getTagName())) {
                    List<HtmlElement> items = list.getByXPath(".//li");
                    for (HtmlElement item : items) {
                        NewsInfo newsInfo = extracULtElementDetails(item, webClient);
                        newsInfo.setNextPageUrl(nextPageUrl);
                        lists1.add(newsInfo);
                    }
                } else if ("tbody".equals(list.getTagName())) {
                    List<HtmlElement> rows = list.getByXPath(".//tr");
                    for (HtmlElement row : rows) {
                        NewsInfo newsInfo = extracTRtElementDetails(row, webClient);
                        lists1.add(newsInfo);
                    }
                }

            }

            System.out.println(JSON.toJSONString(lists1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 公共逻辑提取方法
    private static NewsInfo extracULtElementDetails(HtmlElement htmlElement, WebClient webClient) {
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setTitle(PageExtracUtils.getPageTitle(htmlElement));
        newsInfo.setImgUrl(PageExtracUtils.getPageImg(htmlElement));
        newsInfo.setContent(PageExtracUtils.getPageContent(htmlElement));
        newsInfo.setDate(PageExtracUtils.getPageTime(htmlElement));
        newsInfo.setUrl(PageExtracUtils.getPageUrl(htmlElement, webClient));
        return newsInfo;
    }

    private static NewsInfo extracTRtElementDetails(HtmlElement htmlElement, WebClient webClient) {
        List<HtmlElement> rows = htmlElement.getByXPath("td");
        NewsInfo newsInfo = new NewsInfo();

        for (HtmlElement element : rows) {
            String title = PageExtracUtils.getPageTitle(element);
            String img = PageExtracUtils.getPageImg(element);
            String content = PageExtracUtils.getPageContent(element);
            String time = PageExtracUtils.getPageTime(element);
            String url = PageExtracUtils.getPageUrl(element, webClient);

            boolean otherIsNull = true;
            if (StringUtils.isNotEmpty(title)) {
                otherIsNull = false;
                newsInfo.setTitle(title);
            }
            if (StringUtils.isNotEmpty(img)) {
                otherIsNull = false;

                newsInfo.setImgUrl(img);
            }
            if (StringUtils.isNotEmpty(content)) {
                otherIsNull = false;

                newsInfo.setContent(content);
            }
            if (StringUtils.isNotEmpty(time)) {
                otherIsNull = false;

                newsInfo.setDate(time);
            }
            if (StringUtils.isNotEmpty(url)) {
                otherIsNull = false;

                newsInfo.setUrl(url);
            }
            String author = PageExtracUtils.getPageOther(element, otherIsNull);
            if (StringUtils.isNotEmpty(author)) {
                newsInfo.setAuthor(author);
            }


        }

        return newsInfo;
    }


}
