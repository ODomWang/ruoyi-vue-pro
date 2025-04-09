package cn.wenxun.spider;
import com.microsoft.playwright.*;
import java.util.*;

public class ArticleExtractor {

    public static class Article {
        public String title;
        public String publishTime;
        public String content;

        @Override
        public String toString() {
            return "标题: " + title + "\n时间: " + publishTime + "\n\n正文:\n" + content;
        }
    }

    public static Article extract(String url) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();
            page.navigate(url);
            page.waitForLoadState();

            // 提取正文内容（启发式结构评分，无 class/id 枚举）
            String rawContent = page.evaluate("""
                () => {
                    function visible(el) {
                        const style = window.getComputedStyle(el);
                        return style && style.display !== 'none' && style.visibility !== 'hidden' && el.offsetHeight > 0;
                    }

                    function score(el) {
                        if (!visible(el)) return 0;

                        const rect = el.getBoundingClientRect();
                        const centerY = window.innerHeight / 2;
                        const distanceFromCenter = Math.abs((rect.top + rect.bottom) / 2 - centerY);
                        const verticalScore = Math.max(500 - distanceFromCenter, 0);

                        const text = el.innerText?.trim() || '';
                        const textLen = text.length;
                        const pCount = el.querySelectorAll('p').length;
                        const aCount = el.querySelectorAll('a').length;
                        const linkDensity = aCount / (textLen + 1);

                        if (textLen < 300 || pCount < 2 || linkDensity > 0.2) return 0;

                        let score = textLen + pCount * 50 + verticalScore;
                        score -= aCount * 10;

                        return score;
                    }

                    const candidates = Array.from(document.querySelectorAll('article, section, div'))
                        .filter(el => el.innerText?.length > 200);

                    let best = null;
                    let bestScore = 0;
                    for (const el of candidates) {
                        const s = score(el);
                        if (s > bestScore) {
                            bestScore = s;
                            best = el;
                        }
                    }

                    return best ? best.innerText.trim() : '';
                }
            """).toString();

            String content = cleanTailHeuristically(rawContent);

            // 提取标题
            String title = page.evaluate("""
                () => {
                    const h1s = Array.from(document.querySelectorAll('h1')).filter(h => h.innerText.length > 5);
                    if (h1s.length) return h1s[0].innerText.trim();
                    return document.title || '';
                }
            """).toString();

            // 提取发布时间
            String publishTime = null;
            browser.close();

            Article article = new Article();
            article.title = title;
            article.publishTime = publishTime;
            article.content = content;
            return article;
        }
    }

    private static String cleanTailHeuristically(String rawText) {
        String[] lines = rawText.split("\\n");
        List<String> result = new ArrayList<>();
        int noiseCount = 0;
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].trim();
            boolean isShort = line.length() <= 50;
            boolean isSymbolHeavy = line.matches(".*[|｜：:—－【】<>『』（）()【】].*");
            boolean hasContactPattern = line.matches(".*(\\d{3,4}-\\d{5,}|\\d{3,}-\\d{3,}).*") || line.contains("www") || line.contains("http");
            boolean isLikelyNoise = isShort && (isSymbolHeavy || hasContactPattern);

            if (isLikelyNoise) {
                noiseCount++;
            } else if (noiseCount >= 3) {
                break;
            } else {
                noiseCount = 0;
            }
            result.add(0, line);
        }
        return String.join("\n", result);
    }




    public static void main(String[] args) {
//        String url = "https://news.gzhu.edu.cn/info/1002/33087.htm"; // 替换成测试网址
        String url = "https://www.xuexi.cn/lgpage/detail/index.html?id=17820022691162440613&item_id=17820022691162440613";
        Article data = extract(url);
        System.out.println(data);
    }
}
