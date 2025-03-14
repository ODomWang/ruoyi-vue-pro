package cn.wenxun.admin.job.utils;

import cn.iocoder.yudao.module.system.model.NewsInfo;
import cn.iocoder.yudao.module.system.model.spider.SpiderXpathConfigDO;
import cn.wenxun.admin.core.service.WenXunSpiderCrawlService;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class PlayWrightUtils {


    /**
     * 获取 URL 参数
     */
    public static String getPageUrl(ElementHandle element, Page page) {
        String url = null;
        if (element != null) {
            String tagName = element.evaluate("node => node.tagName").toString().toLowerCase();
            if (("a").equals(tagName)) {
                url = element.getAttribute("href");
                String onclick = element.getAttribute("onclick");
                if (url != null && url.contains("javascript:")) {
                    url = "";
                }
                if (StringUtils.isEmpty(url) && StringUtils.isNotEmpty(onclick)) {
                    element.click();
                    page.waitForLoadState(LoadState.NETWORKIDLE);
                    url = page.url();
                }
            } else {
                ElementHandle linkElement = element.querySelector("a");
                if (linkElement != null) {
                    url = linkElement.getAttribute("href");
                    String onclick = linkElement.getAttribute("onclick");
                    if (url != null && url.contains("javascript:")) {
                        url = "";
                    }
                    if (StringUtils.isEmpty(url) && StringUtils.isNotEmpty(onclick)) {
                        linkElement.click();
                        page.waitForLoadState(LoadState.NETWORKIDLE);
                        url = page.url();
                    }
                }
            }
        }
        if (StringUtils.isEmpty(url)) {
            log.error("链接地址找不到：【" + element.innerHTML() + "】");
            return null;
        }
        return ensureAbsoluteUrl(page.url(), url);
    }


    /**
     * 拼接完整 URL
     */
    public static String ensureAbsoluteUrl(String baseUrl, String href) {
        try {
            URL url = new URL(href);
            return url.toString();
        } catch (MalformedURLException e) {
            try {
                URL fullUrl = new URL(new URL(baseUrl), href);
                return fullUrl.toString();
            } catch (MalformedURLException ex) {
                System.err.println("无法解析 URL: " + href);
                return href;
            }
        }
    }


    public static List<NewsInfo> crawlUrl(SpiderXpathConfigDO xpathConfigDO, WenXunSpiderCrawlService wenXunSpiderCrawlService, boolean isTest) {


        try (Playwright playwright = Playwright.create()) {
            BrowserType browserType = playwright.chromium();
            Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            String Url = xpathConfigDO.getSpiderUrl();
            List<NewsInfo> newsList = new ArrayList<>();

            for (long i = 0; i < xpathConfigDO.getSpiderPageNum(); i++) {
                log.info("----采集：【" + xpathConfigDO.getSpiderName() + "】 第【" + i + "】页----");

                // 加载网页
                page.navigate(Url);
                page.waitForLoadState(LoadState.NETWORKIDLE);


                // 提取网页图标
                ElementHandle iconElement = page.querySelector("link[rel='icon']");
                String iconUrl = iconElement != null ? iconElement.getAttribute("href") : "";

                // 提取下一页链接
                // 提取下一页链接

                Locator nextPageLocator = page.locator("xpath=" + xpathConfigDO.getNextPageXpath());
                String nextPageUrl = "";
                if (nextPageLocator.count() > 0) {
                    nextPageUrl = PlayWrightUtils.getPageUrl(nextPageLocator.first().elementHandle(), page);
                }

                // 提取列表页面内容


                Locator listsLocator = page.locator("xpath=" + xpathConfigDO.getListXpath());
                List<ElementHandle> lists = listsLocator.elementHandles();

                for (ElementHandle list : lists) {
                    List<ElementHandle> lists1_1 = list.querySelectorAll("xpath=" + xpathConfigDO.getItemXpath());

                    for (ElementHandle elementHandle : lists1_1) {
                        NewsInfo newsInfo = new NewsInfo();
                        if (StringUtils.isNotEmpty(xpathConfigDO.getTitleXpath())) {
                            // 提取文章块
                            // 提取标题
                            ElementHandle tileelement = elementHandle.querySelector("xpath=" + xpathConfigDO.getTitleXpath());
                            if (tileelement != null) {
                                String tile = tileelement.textContent();
                                newsInfo.setTitle(tile);
                            }
                        }
                        if (StringUtils.isNotEmpty(xpathConfigDO.getDescXpath())) {
                            // 提取描述
                            ElementHandle tiledesc = elementHandle.querySelector("xpath=" + xpathConfigDO.getDescXpath());
                            if (tiledesc != null) {
                                String tile = tiledesc.textContent();
                                newsInfo.setDesc(tile);
                            }
                        }
                        newsInfo.setUrl(PlayWrightUtils.getPageUrl(elementHandle, page));
                        newsInfo.setWebIcon(iconUrl);
                        String content = extractContentFromPage(newsInfo.getUrl(), page.context(), xpathConfigDO.getBodyXpath());
                        newsInfo.setNextPageUrl(nextPageUrl);
                        newsInfo.setContent(content);
                        newsInfo.setSpiderName(xpathConfigDO.getSpiderName());
                        newsInfo.setConfigId(xpathConfigDO.getId());
                        newsInfo.setDeptId(xpathConfigDO.getDeptId());
                        newsList.add(newsInfo);
                        if (!isTest) {
                            wenXunSpiderCrawlService.insertDoBySpider(List.of(newsInfo));
                        }
                    }
                }
                Url = nextPageUrl;
                if (StringUtils.isEmpty(Url)) {
                    log.info("下一页不存在，结束当前网站采集任务【" + xpathConfigDO.getSpiderName() + "】");
                    break;
                }

            }
            // 根据 name 字段去重

            return new ArrayList<>(newsList.stream()
                    .collect(Collectors.toMap(
                            NewsInfo::getUrl,  // 使用 name 字段作为唯一标识
                            info -> info,  // 保留整个 Person 对象
                            (existing, replacement) -> existing // 如果 name 相同，保留第一个对象
                    ))
                    .values());
        }
    }

    private static String extractContentFromPage(String url, BrowserContext context, String bodyXpath) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        Page tempPage = context.newPage();
        tempPage.navigate(url);
        tempPage.waitForLoadState(LoadState.NETWORKIDLE);

        ElementHandle bodyElement = tempPage.querySelector("xpath=" + bodyXpath);
        String content = bodyElement != null ? bodyElement.innerHTML() : "";
        tempPage.close();
        return content;
    }


    public static String convert(String content) {
        try {


            Pattern p_script, p_style, p_html, p_html1;
            Matcher m_script, m_style, m_html, m_html1;

            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String regEx_html1 = "<[^>]+";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(content);
            content = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(content);
            content = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(content);
            content = m_html.replaceAll(""); // 过滤html标签

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(content);
            content = m_html1.replaceAll(""); // 过滤html标签

            return content;// 返回文本字符串
        } catch (Exception e) {
            return content;
        }
    }
}
