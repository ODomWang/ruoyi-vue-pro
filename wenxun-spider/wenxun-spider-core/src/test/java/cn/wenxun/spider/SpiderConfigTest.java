package cn.wenxun.spider;

import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.stream.Collectors;

public class SpiderConfigTest implements PageProcessor {
    private Site site = Site.me().setRetryTimes(1).setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36").setSleepTime(100);

    @Override
    public void process(Page page) {

        List<String> url = page.getHtml().links().all();
        if (!CollectionUtils.isEmpty(url)) {
            url = url.stream().distinct().collect(Collectors.toList());
        }
        page.addTargetRequests(url);
        page.putField("news_title", page.getHtml().xpath("//meta[@name='pageTitle']/@content").get());
        page.putField("news_keywords", page.getHtml().xpath("//meta[@name='keywords']/@content").get());
        page.putField("news_description", page.getHtml().xpath("//meta[@name='description']/@content").all());
        page.putField("news_detail", page.getHtml().xpath("//div[@id='vsb_content']//span//text()").all());
        page.putField("news_time", page.getHtml().xpath("//div[@class='time']//span//text()").all());
        if( page.getHtml().xpath("//div[@id='vsb_content']//span//text()").all().isEmpty()){
            System.out.println(1);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        /**
         *  获取键盘输入两个参数 url和标题
         */



        Spider.create(new SpiderConfigTest()).addUrl("https://www.bzpt.edu.cn").thread(1).run();
    }
}