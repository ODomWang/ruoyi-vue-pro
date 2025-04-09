package cn.wenxun.spider;

import com.microsoft.playwright.*;

import java.util.*;
import java.util.regex.*;

public class PlaywrightExample {
    public static void main(String[] args) {
        List<String> urls = Arrays.asList(
                "https://www.nbpt.edu.cn/nzyw/list.htm",
                "https://www.wxstc.cn/zxxw/kyyw.htm",
                "https://www.jssc.edu.cn/98/list.htm",
                "https://www.hzvtc.edu.cn/xxyw.htm",
                "https://www.htc.edu.cn/xwgg/xxxw.htm",
                "https://news.wtc.edu.cn/335/list.htm",
                "https://www.scvtc.edu.cn/ggfw1/xyxw.htm",
                "https://www.uta.edu.cn/_s3/1111/list.psp",
                "https://xzyedu.com.cn/index/xzyw.htm",
                "http://www.lctvu.sd.cn/xwzx/xyxw.htm",
                "https://news.gzhu.edu.cn/ttgd.htm"
               );
        for (String url : urls) {
            System.out.println("======================开始采集=====：" + url + "======================");

            try (Playwright playwright = Playwright.create()) {
                Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
                Page page = browser.newPage();
                page.navigate(url);
                page.waitForLoadState();

                // 通用日期正则
                Pattern datePattern = Pattern.compile(".*(\\d{4}[-./年]\\d{1,2}[-./月]\\d{1,2}|\\d{1,2}[-./]\\d{1,2}|\\d+小时前|\\d+分钟前).*");

                // 扫描全页面潜在的容器
                List<ElementHandle> allElements = page.querySelectorAll("ul, ol, div, section");

                List<ElementHandle> bestCandidates = new ArrayList<>();
                int bestScore = 0;

                for (ElementHandle container : allElements) {
                    if(container.innerHTML().contains("我校获得")){
                        System.out.println(1);
                    }
                    List<ElementHandle> children = container.querySelectorAll(":scope > *");

                    if (children.size() < 3 || children.size() > 100) continue;

                    int valid = 0;
                    int score = 0;

                    for (ElementHandle element : children) {
                        ElementHandle link = element;
                        if (!"a".equalsIgnoreCase((String) element.evaluate("el => el.tagName.toLowerCase()"))) {
                            link = element.querySelector("a");
                        }
                        if (link == null) continue;

                        String title = link.textContent().trim();
                        if (title.length() < 6) continue;

                        String href = link.getAttribute("href");
                        if (href == null || href.contains("javascript")) continue;

                        String time = "";
                        for (ElementHandle sub : element.querySelectorAll("*")) {
                            String txt = sub.innerText().trim();
                            if (datePattern.matcher(txt).matches()) {
                                time = txt;
                                break;
                            }
                        }
                        if (!time.isEmpty()) score += 10;

                        valid++;
                    }

                    if (valid >= children.size() * 0.5 && score > bestScore) {
                        bestCandidates = children;
                        bestScore = score;
                    }
                }

                // 若无法识别结构，则尝试扁平 <a> 提取（Plan B）
                if (bestCandidates.isEmpty()) {
                    bestCandidates = page.querySelectorAll("a");
                }

                List<Map<String, String>> articles = new ArrayList<>();

                for (ElementHandle element : bestCandidates) {
                    ElementHandle link = element;
                    if (!"a".equalsIgnoreCase((String) element.evaluate("el => el.tagName.toLowerCase()"))) {
                        link = element.querySelector("a");
                    }
                    if (link == null) continue;

                    String title = link.textContent().trim();
                    if (title.length() < 6) continue;

                    String href = link.getAttribute("href");
                    if (href == null || href.contains("javascript")) continue;
                    if (!href.startsWith("http")) {
                        href = absoluteUrl(page.url(), href);
                    }

                    String time = "";
                    for (ElementHandle sub : element.querySelectorAll("*")) {
                        String txt = sub.innerText().trim();
                        if (datePattern.matcher(txt).matches()) {
                            time = txt;
                            break;
                        }
                    }

                    if (time.isEmpty()) continue;

                    Map<String, String> article = new HashMap<>();
                    article.put("title", title);
                    article.put("link", href);
                    article.put("time", time);
                    articles.add(article);
                }

                if (articles.isEmpty()) {
                    System.out.println("❌ 无法提取文章项");
                } else {
                    System.out.println("✅ 提取到文章 " + articles.size() + " 条：");

                    for (Map<String, String> article : articles) {
                        System.out.println("----");
                        System.out.println("标题：" + article.get("title"));
                        System.out.println("链接：" + article.get("link"));
//                        ArticleExtractor.ArticleData data = ArticleExtractor.extractArticle( article.get("link"));
//                        System.out.println(data);
                        System.out.println("时间：" + article.get("time"));
                    }
                }

                browser.close();
            }
        }
    }

    private static String absoluteUrl(String base, String rel) {
        if (rel == null) return "";
        if (rel.startsWith("http")) return rel;
        if (rel.startsWith("//")) return "https:" + rel;
        if (rel.startsWith("/")) return base.replaceAll("(https?://[^/]+).*", "$1") + rel;
        return base.substring(0, base.lastIndexOf("/") + 1) + rel;
    }
}
