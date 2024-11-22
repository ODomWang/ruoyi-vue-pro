package cn.wenxun.spider;


import cn.wenxun.admin.job.utils.HtmlUnitUtil;
import cn.wenxun.admin.job.utils.PageExtracUtils;
import cn.wenxun.admin.model.NewsInfo;
import cn.wenxun.admin.model.spider.WenxunSpiderSourceConfigDO;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlUnitExample {
    public static void main(String[] args) {

//          创建 WebClient
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
            HtmlPage page = webClient.getPage("https://www.bzpt.edu.cn/xwdr/xyyw.htm");  // 滨州职业学院
//            HtmlPage page = webClient.getPage("https://www.suda.edu.cn/suda_news/sdyw/index.html");  // 苏州大学
//            HtmlPage page = webClient.getPage("http://www.wxcu.edu.cn/static/newList.html?cid=19");  // 无锡城市学院
            webClient.waitForBackgroundJavaScript(10000); // 等待 JavaScript 执行完成，最多等待 10 秒

            if (page.asXml().contains("window.onload();")) {
                page.executeJavaScript("if (window.onload) window.onload();");
            }
            // 提取网页图标
            String iconUrl = PageExtracUtils.getPageIcon(page);
            String nextPageUrl = PageExtracUtils.getNextPageUrl(page, "//*[@id=\"app\"]/div[4]/div/div[2]/span/span[10]/a");
            List<HtmlElement> lists = page.getByXPath("//*[@id=\"app\"]/div[4]/div/ul");

            List<NewsInfo> lists1 = new ArrayList<>();
            for (HtmlElement list : lists) {
                // 判断是 <ul> 还是 <table>
                if ("ul".equals(list.getTagName())) {
                    List<HtmlElement> items = list.getByXPath(".//li");
                    for (HtmlElement item : items) {
                        NewsInfo newsInfo = extracULtElementDetails(item, webClient);
                        String content = extractContentFromPage(newsInfo.getUrl(), newsInfo.getTitle(), webClient);
                        newsInfo.setNextPageUrl(nextPageUrl);
                        newsInfo.setContent(content);
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
            String commonParent = extractAndPrintContent(bodyElement, titleText);
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
    private static String extractAndPrintContent(HtmlElement rootElement, String title) {
        // 只在 body 元素下遍历，识别并提取标题、正文、时间、作者和图片
        if (title.endsWith("...")) {
            title = title.substring(0, title.length() - 3);
        }
        HtmlElement htmlElement = findElementContainingText(rootElement, title);
        HtmlForm nform = findParentForm(htmlElement);
        String mkd = htmlToMarkdown(nform.asXml());
//        String filteredContent = filterScriptTags(nform);
        return mkd;
    }

    private static String filterScriptTags(HtmlElement element) {
        StringBuilder content = new StringBuilder();
        for (DomNode node : element.getChildNodes()) {
            if (node instanceof HtmlScript) {
                // 跳过 script 标签
                continue;
            } else if (node instanceof HtmlImage) {
                // 提取图片链接
                HtmlImage image = (HtmlImage) node;
                content.append("Image: ").append(image.getSrcAttribute()).append("\n");
            } else if (node instanceof HtmlElement) {
                // 递归处理子元素
                content.append(filterScriptTags((HtmlElement) node));
            } else {
                // 添加文本节点内容
                content.append(node.getTextContent());
            }
        }
        return content.toString();
    }

    // 判断文本是否为日期的方法
    private static boolean isDate(String text) {
        // 简单判断是否为日期，可以根据需要调整正则表达式
        return text.matches("\\d{4}-\\d{2}-\\d{2}.*");
    }

    // 判断文本是否为作者的方法
    private static boolean isAuthor(String text) {
        // 简单判断是否为作者，可以根据需要调整逻辑
        return text.toLowerCase().contains("author") || text.toLowerCase().contains("by");
    }

    public static String htmlToMarkdown(String html) {
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
            src = PageExtracUtils.ensureAbsoluteUrl("https://www.bzpt.edu.cn", src);
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
