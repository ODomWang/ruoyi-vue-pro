package cn.wenxun.admin.job.utils;


import cn.wenxun.admin.model.NewsInfo;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HtmlUnitUtil {

    public static List<NewsInfo> crawlUrl(WenxunSpiderSourceConfigDO configDO, boolean test) {
        // 创建 WebClient
        List<NewsInfo> respNewsInfo = new ArrayList<>();

        try (final WebClient webClient = new WebClient()) {
            // 禁用 JavaScript 和 CSS 支持（根据页面需求，如果需要可以启用）
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setDoNotTrackEnabled(false);

            // 设置等待时间
            webClient.getOptions().setTimeout(5000);

            // 加载网页
            String Url = configDO.getSpiderUrl();

            for (long i = 0; i < configDO.getSpiderPageNum(); i++) {

                HtmlPage page = webClient.getPage(Url);  // 目标网址
                webClient.waitForBackgroundJavaScript(10000); // 等待 JavaScript 执行完成，最多等待 5 秒

                if (page.asXml().contains("window.onload();")) {
                    page.executeJavaScript("if (window.onload) window.onload();");
                }
                // 提取网页图标
                String iconUrl = PageExtracUtils.getPageIcon(page);
//                 下一页地址
                Url = PageExtracUtils.getNextPageUrl(page, configDO.getNextPageXpath());

                List<HtmlElement> lists = page.getByXPath(configDO.getBodyXpath());

                for (HtmlElement list : lists) {
                    // 判断是 <ul> 还是 <table>
                    if ("ul".equals(list.getTagName())) {
                        List<HtmlElement> items = list.getByXPath(".//li");
                        for (HtmlElement item : items) {
                            NewsInfo newsInfo = extracULtElementDetails(item, webClient);
                            newsInfo.setConfigId(configDO.getId());
                            String content = extractContentFromPage(newsInfo.getUrl(), newsInfo.getTitle(), webClient);
                            newsInfo.setContent(content);
                            newsInfo.setWebIcon(iconUrl);
                            respNewsInfo.add(newsInfo);
                            if (test) {
                                return respNewsInfo;
                            }
                        }
                    } else if ("tbody".equals(list.getTagName())) {
                        List<HtmlElement> rows = list.getByXPath(".//tr");
                        for (HtmlElement row : rows) {
                            NewsInfo newsInfo = extracTRtElementDetails(row, webClient);
                            newsInfo.setConfigId(configDO.getId());
                            newsInfo.setWebIcon(iconUrl);
                            respNewsInfo.add(newsInfo);
                            if (test) {
                                return respNewsInfo;
                            }
                        }
                    }

                }
            }
            return respNewsInfo;
        } catch (IOException e) {
           e.printStackTrace();
        }
        return respNewsInfo;
    }

    // 公共逻辑提取方法
    private static NewsInfo extracULtElementDetails(HtmlElement htmlElement, WebClient webClient) {
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setTitle(PageExtracUtils.getPageTitle(htmlElement));
        newsInfo.setImgUrl(PageExtracUtils.getPageImg(htmlElement));
        newsInfo.setDesc(PageExtracUtils.getPageContent(htmlElement));
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

    // 包装后的主方法：根据输入的 URL、标题和正文文本进行内容提取
    public static String extractContentFromPage(String url, String titleText, WebClient webClient) throws IOException {
        // 加载页面
        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(3000);

        if (page != null) {
            HtmlElement bodyElement = page.getBody();
            String commonParent = extractAndPrintContent(bodyElement, titleText,url);
            return commonParent;
        }
        return null;
    }

    // 查找包含特定文本的元素的方法
    private static HtmlElement findElementContainingText(HtmlElement rootElement, String text) {
        List<HtmlElement> elements = rootElement.getByXPath(".//*[contains(text(), '" + text + "')]");
        return elements.isEmpty() ? null : elements.get(0);
    }

    // 查找元素的父级 form 标签的方法
    private static HtmlForm findParentForm(HtmlElement element) {
        HtmlElement current = element;
        while (current != null) {
            if (current instanceof HtmlForm) {
                return (HtmlForm) current;
            }
            current = (HtmlElement) current.getParentNode();
        }
        return null;
    }

    // 从页面中提取标题、正文、时间、作者和图片的方法
    private static String extractAndPrintContent(HtmlElement rootElement, String title,String url) {
        // 只在 body 元素下遍历，识别并提取标题、正文、时间、作者和图片
        if (title.endsWith("...")) {
            title = title.substring(0, title.length() - 3);
        }
        HtmlElement htmlElement = findElementContainingText(rootElement, title);
        HtmlForm nform = findParentForm(htmlElement);
        String mkd = htmlToMarkdown(nform.asXml(),url);
//        String filteredContent = filterScriptTags(nform);
        return mkd;
    }

    public static String htmlToMarkdown(String html,String baseUrl) {
        Document document = Jsoup.parse(html);
        StringBuilder markdown = new StringBuilder();

        // Convert headings
        for (int i = 1; i <= 6; i++) {
            Elements headings = document.select("h" + i);
            for (Element heading : headings) {
                markdown.append("#".repeat(i)).append(" ").append(heading.text()).append("\n\n");
            }
        }

        // Convert paragraphs
        Elements paragraphs = document.select("p");
        for (Element paragraph : paragraphs) {
            markdown.append(paragraph.text()).append("\n\n");
        }

        // Convert links
        Elements links = document.select("a");
        for (Element link : links) {
            String url = link.attr("href");
            String text = link.text();
            markdown.append("[" + text + "](" + url + ")").append("\n\n");
        }

        // Convert images
        Elements images = document.select("img");
        for (Element image : images) {
            String src = image.attr("src");
            String alt = image.attr("alt");
            src = PageExtracUtils.ensureAbsoluteUrl(baseUrl, src);
            markdown.append("![](" + src + ")").append("\n\n");
        }

        // Convert tables
        Elements tables = document.select("table");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements headers = row.select("th");
                Elements cells = row.select("td");

                if (!headers.isEmpty()) {
                    for (Element header : headers) {
                        markdown.append("| ").append(header.text()).append(" ");
                    }
                    markdown.append("|\n");
                    markdown.append("|".repeat(headers.size())).append(" --- |\n");
                }

                if (!cells.isEmpty()) {
                    for (Element cell : cells) {
                        markdown.append("| ").append(cell.text()).append(" ");
                    }
                    markdown.append("|\n");
                }
            }
            markdown.append("\n");
        }

        return markdown.toString();
    }

}
