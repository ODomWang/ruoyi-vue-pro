package cn.wenxun.spider;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.core.Seimi;
import cn.wanghaomiao.seimi.core.SeimiCrawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.spring.common.CrawlerCache;
import cn.wanghaomiao.seimi.struct.CrawlerModel;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import org.apache.commons.lang3.StringUtils;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Crawler(name = "htmlExtractor")
public class HtmlExtractor   extends BaseSeimiCrawler {
    @Override
    public String getUserAgent() {
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
    }

    /**
     * @return
     */
    @Override
    public String proxy() {
        return null;
    }

    @Override
    public String[] startUrls() {
        return new String[]{"http://example.com"};
    }

    /**
     * @return
     */
    @Override
    public List<Request> startRequests() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public String[] allowRules() {
        return new String[0];
    }

    /**
     * @return
     */
    @Override
    public String[] denyRules() {
        return new String[0];
    }

    @Override
    public void start(Response response) {
        JXDocument doc = response.document();

        // 提取 <ul> 或 <table> 元素中的文本
        List<JXNode> lists = doc.selN("//*[@id='app']/div[4]/div/*[self::ul or self::table] | /html/body/table[3]/tbody/tr/td[2]/table[3]/tbody");
        if (lists.isEmpty()) {
            System.out.println("未找到匹配的元素。");
        } else {
            for (JXNode listNode : lists) {
                if (listNode.isElement()) {
                    String tagName = listNode.asElement().tagName();
                    if ("ul".equals(tagName)) {
                        List<JXNode> items = listNode.sel("li");
                        for (JXNode item : items) {
                            extractElementDetails(item);
                        }
                    } else if ("table".equals(tagName)) {
                        List<JXNode> rows = listNode.sel("tr");
                        for (JXNode row : rows) {
                            extractElementDetails(row);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param request
     */
    @Override
    public void handleErrorRequest(Request request) {

    }

    // 公共逻辑提取方法
    private static void extractElementDetails(JXNode element) {
        if (!element.isElement()) {
            return;
        }

        // 获取元素的完整源码信息
        String elementSource = element.asElement().toString();
        System.out.println("元素完整源码信息：\n" + elementSource);

        JXNode titleElement = null;
        for (int i = 1; i <= 6; i++) {
            titleElement = element.selOne(".//h" + i);
            if (titleElement != null && titleElement.isElement()) {
                break;
            }
        }
        JXNode imageElement = element.selOne(".//img");
        JXNode textElement = element.selOne(".//p");
        JXNode timeElement = element.selOne(".//time");
        JXNode urlElement = element.selOne(".//a");

        String title = titleElement != null ? titleElement.asElement().text() : null;
        if (StringUtils.isEmpty(title) && urlElement != null && urlElement.isElement()) {
            title = StringUtils.isNotEmpty(urlElement.asElement().attr("title")) ? urlElement.asElement().attr("title") : urlElement.asElement().text();
        }

        String image = imageElement != null && imageElement.isElement() ? (imageElement.asElement().attr("src").isEmpty() ? imageElement.asElement().attr("data-original") : imageElement.asElement().attr("src")) : null;
        String text = textElement != null && textElement.isElement() ? textElement.asElement().text() : null;
        String time = timeElement != null && timeElement.isElement() ? timeElement.asElement().text() : null;
        String url = urlElement != null && urlElement.isElement() ? urlElement.asElement().attr("href") : null;

        if (time != null && !isValidTimeFormat(time)) {
            time = "N/A";
        }

        System.out.println("标题：" + (title != null ? title : "N/A"));
        System.out.println("图片：" + (image != null ? image : "N/A"));
        System.out.println("正文：" + (text != null ? text : "N/A"));
        System.out.println("时间：" + (time != null ? time : "N/A"));
        System.out.println("url：" + (url != null ? url : "N/A"));
        System.out.println("");
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
                // 忽略异常，继续尝试下一个格式
            }
        }
        return false;
    }

    public static void main(String[] args) {

        Seimi s = new Seimi();
        s.start("htmlExtractor");
    }
}
