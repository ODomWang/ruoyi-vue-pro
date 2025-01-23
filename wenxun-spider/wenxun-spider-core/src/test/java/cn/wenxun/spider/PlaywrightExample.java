package cn.wenxun.spider;

import cn.wenxun.admin.job.utils.PlayWrightUtils;
import cn.iocoder.yudao.module.wenxun.model.NewsInfo;
import cn.iocoder.yudao.module.wenxun.model.spider.SpiderXpathConfigDO;
import com.alibaba.fastjson.JSON;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import java.util.ArrayList;
import java.util.List;

public class PlaywrightExample {
    public static void main(String[] args) {
        SpiderXpathConfigDO xpathConfigDO = SpiderXpathConfigDO.builder()
                .spiderUrl("http://dangjian.people.com.cn/index1.html")
                .spiderName("人民网")
                .spiderModel("人民网")
                .remark("人民网")
                .spiderPageNum(1L)
                .bodyXpath("/html[1]/body[1]/div[6]/div[1]")
                .nextPageXpath("/html[1]/body[1]/div[4]/div[1]/div[2]/div[9]/a[5]")
                .listXpath("/html[1]/body[1]/div[4]/div[1]/div[1]")
                .itemXpath("/html[1]/body[1]/div[4]/div[1]/div[1]/div[1]/p[1]")
                .titleXpath("/html[1]/body[1]/div[4]/div[1]/div[2]/div[7]/p[1]/strong[1]")
                .descXpath("/html[1]/body[1]/div[4]/div[1]/div[1]/div[1]/p[1]/a[1]/em[1]").build();
        System.out.println(crawlUrl(xpathConfigDO));

    }

    public static List<NewsInfo> crawlUrl(SpiderXpathConfigDO xpathConfigDO) {


        try (Playwright playwright = Playwright.create()) {
            BrowserType browserType = playwright.chromium();
            Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // 加载网页
            page.navigate(xpathConfigDO.getSpiderUrl());
            page.waitForLoadState(LoadState.NETWORKIDLE);


            // 提取网页图标
            ElementHandle iconElement = page.querySelector("link[rel='icon']");
            String iconUrl = iconElement != null ? iconElement.getAttribute("href") : "";

            // 提取下一页链接
            // 提取下一页链接

            Locator nextPageLocator = page.locator("xpath="+xpathConfigDO.getNextPageXpath());
            String nextPageUrl = "";
            if (nextPageLocator.count() > 0) {
                nextPageUrl = PlayWrightUtils.getPageUrl(nextPageLocator.first().elementHandle(), page);
            }
            // 提取列表页面内容


            Locator listsLocator = page.locator("xpath="+xpathConfigDO.getListXpath());
            List<ElementHandle> lists = listsLocator.elementHandles();

            List<NewsInfo> newsList = new ArrayList<>();
            for (ElementHandle list : lists) {
                List<ElementHandle> lists1_1 = list.querySelectorAll("xpath="+xpathConfigDO.getItemXpath());

                for (ElementHandle elementHandle : lists1_1) {
                    NewsInfo newsInfo = new NewsInfo();

                    // 提取文章块
                    // 提取标题
                    ElementHandle tileelement = elementHandle.querySelector("xpath="+xpathConfigDO.getTitleXpath());
                    if (tileelement != null) {
                        String tile = tileelement.innerText();
                        newsInfo.setTitle(tile);
                    }

                    // 提取描述
                    ElementHandle tiledesc = elementHandle.querySelector("xpath="+xpathConfigDO.getDescXpath());
                    if (tiledesc != null) {
                        String tile = tiledesc.innerText();
                        newsInfo.setDesc(tile);
                    }
                    newsInfo.setUrl(PlayWrightUtils.getPageUrl(elementHandle, page));

                    String content = extractContentFromPage(newsInfo.getUrl(), page.context(),xpathConfigDO.getBodyXpath());
                    newsInfo.setNextPageUrl(nextPageUrl);
                    newsInfo.setContent(content);
                    newsList.add(newsInfo);
                }
            }
            System.out.println(JSON.toJSONString(newsList));
            return(newsList);
        }
    }

    private static String extractContentFromPage(String url, BrowserContext context,String bodyXpath) {
        Page tempPage = context.newPage();
        tempPage.navigate(url);
        tempPage.waitForLoadState(LoadState.NETWORKIDLE);

        ElementHandle bodyElement = tempPage.querySelector("xpath="+bodyXpath);
        String content = bodyElement != null ? bodyElement.innerText() : "";
        tempPage.close();
        return content;
    }

}
