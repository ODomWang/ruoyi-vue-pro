package cn.wenxun.admin.job.utils;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PageExtracUtils {


    /*
     * 获取url参数
     */
    public static String getPageUrl(HtmlElement htmlElement, WebClient webClient) {
        String url = null;
        if (htmlElement != null) {
            HtmlElement urlElement = htmlElement.getFirstByXPath(".//a");
            if (urlElement != null) {
                // 获取 href 属性
                url = urlElement.getAttribute("href");
                String onclick = urlElement.getAttribute("onclick");
                // 如果 href 是 JavaScript 代码，则清空 url
                if (url.contains("javascript:")) {
                    url = "";
                }
                // 如果 url 为空且 onclick 不为空，则点击元素获取新的页面 URL
                if (StringUtils.isEmpty(url) && StringUtils.isNotEmpty(onclick)) {
                    HtmlPage newPage = null;
                    try {
                        newPage = urlElement.click();
                        webClient.waitForBackgroundJavaScript(1000); // 等待背景 JavaScript 执行
                        url = newPage.getUrl().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        url = ensureAbsoluteUrl(htmlElement.getBaseURI(), url);

        return url;
    }


    /**
     * 获取标题参数
     *
     * @param htmlElement
     */
    public static String getPageTitle(HtmlElement htmlElement) {
        String title = null;
        if (htmlElement != null) {
            HtmlElement titleElement = htmlElement.getFirstByXPath(".//h1 | .//h2 | .//h3 | .//h4 | .//h5 | .//h6");
            title = titleElement != null ? titleElement.getTextContent() : null;

            HtmlElement _aElement = htmlElement.getFirstByXPath(".//a");
            if (StringUtils.isEmpty(title) && _aElement != null) {
                title = StringUtils.isNotEmpty(_aElement.getAttribute("title")) ? _aElement.getAttribute("title") : _aElement.getTextContent();
            }
        }
        return title;
    }

    /**
     * 获取图片参数
     */
    public static String getPageImg(HtmlElement htmlElement) {
        String imgUrl = null;
        if (htmlElement != null) {
            HtmlElement imgElement = htmlElement.getFirstByXPath(".//img");
            if (imgElement != null) {
                imgUrl = imgElement.getAttribute("data-original");
                if (imgUrl.isEmpty()) {
                    imgUrl = imgElement.getAttribute("src");

                }
            }
        }
        imgUrl = ensureAbsoluteUrl(htmlElement.getBaseURI(), imgUrl);
        return imgUrl;
    }

    /**
     * 获取正文
     */
    public static String getPageContent(HtmlElement htmlElement) {
        String content = null;
        if (htmlElement != null) {
            HtmlElement contentElement = htmlElement.getFirstByXPath(".//p");
            content = contentElement != null ? contentElement.getTextContent() : null;
        }
        return content;
    }

    /**
     * 获取时间
     */
    public static String getPageTime(HtmlElement htmlElement) {
        String time = null;
        if (htmlElement != null) {
            List<HtmlElement> elements = htmlElement.getByXPath(".//time | .//date | .//span");
            // 遍历并输出结果
            for (HtmlElement element : elements) {
                if (isValidTimeFormat(element.getTextContent())) {
                    return element.getTextContent();
                }
            }
            if (StringUtils.isEmpty(time) && isValidTimeFormat(htmlElement.getTextContent())) {
                time = htmlElement.getTextContent();
            }
        }
        return time;
    }

    /**
     * 获取提取不到的标签
     *
     * @param
     * @return
     */
    public static String getPageOther(HtmlElement htmlElement, boolean otherIsNull) {
        String other = null;
        if (htmlElement != null && otherIsNull) {
            other = htmlElement.getTextContent();
            if (other.contains("function") || other.contains("首页")) {
                return null;
            }
        }
        return other;
    }

    /**
     * 获取下一页url
     *
     * @param htmlElement
     * @return
     */
    public static String getNextPageUrl(HtmlPage htmlElement, String nextPageXpath) {
        String nextUrl = null;
        if (htmlElement != null) {
            HtmlElement element = htmlElement.getFirstByXPath(nextPageXpath);
            nextUrl = element != null ? element.getAttribute("href") : null;
        }
        nextUrl = ensureAbsoluteUrl(htmlElement.getBaseURI(), nextUrl);

        return nextUrl;
    }


    // 判断字符串是否为时间格式
    private static boolean isValidTimeFormat(String time) {
        String[] formats = {"yyyy-MM-dd", "yyyy/MM/dd", "dd-MM-yyyy", "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"};
        for (String format : formats) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            try {
                sdf.parse(time);
                return true;
            } catch (ParseException e) {

            }
        }
        return false;
    }

    // 判断并拼接完整 URL 的方法
    private static String ensureAbsoluteUrl(String baseUrl, String href) {
        try {
            // 如果 href 是完整 URL，直接返回
            URL url = new URL(href);
            return url.toString();
        } catch (MalformedURLException e) {
            // 如果 href 不是完整 URL，则拼接基础 URL
            try {
                URL fullUrl = new URL(new URL(baseUrl), href);
                return fullUrl.toString();
            } catch (MalformedURLException ex) {
                // 返回空或原始 href 以应对解析失败的情况
                System.err.println("无法解析 URL: " + href);
                return href;
            }
        }
    }

}
