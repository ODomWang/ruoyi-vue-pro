package cn.wenxun.spider;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
import com.xuxueli.crawler.parser.PageParser;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class xxlCrawl {
    public static void main(String[] args) {
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.bzpt.edu.cn/xwdr/xyyw.htm")
                .setWhiteUrlRegexs("https://www.bzpt.edu.cn/info/.*")
                .setThreadCount(3)

                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, PageVo pageVo) {
                        // 解析封装 PageVo 对象

                        String pageUrl = html.baseUri();
                        System.out.println(pageUrl + "：" + pageVo.toString());
                    }
                })
                .build();
        crawler.start(true);


    }

    @Data
    // PageSelect 注解：从页面中抽取出一个或多个VO对象；
    @PageSelect(cssQuery = "#app > div.nyMain.nyarcList > div > ul")
    public static class PageVo {
        @PageFieldSelect(cssQuery = "#line_u9_0 > a > div.txt > h4")
        private String title;
        @PageFieldSelect(cssQuery = "#line_u9_0 > a > div.txt > time")
        private String comment;
        // set get
    }
}
