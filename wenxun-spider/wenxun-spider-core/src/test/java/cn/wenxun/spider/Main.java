package cn.wenxun.spider;


import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class Main {

    @SneakyThrows
    public static void main(String[] args) throws IOException {
        //获取用户信息
//        WebClient webClient = new WebClient(BrowserVersion.EDGE);
//        // 关闭不需要的日志和选项
//        webClient.getOptions().setCssEnabled(false);
//        webClient.getOptions().setJavaScriptEnabled(false);
//
        String url1 = "https://jou.91job.org.cn/sub-station/notificationList?xxdm=11641&lmid=4342";
//        // 加载页面
//        webClient.getOptions().setUseInsecureSSL(true);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
//        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
//        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
//        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
//        webClient.waitForBackgroundJavaScript(30000);
//        HtmlPage page = webClient.getPage(url1);
//        String resp = SpiderAiUtils.SpiderByOpenAi(page.asXml(), "获取网站的就业新闻列表", Collections.singleton(new NewsInfo()), "https://jou.91job.org.cn");
//        System.out.println(resp);
        // 设置 ChromeDriver 的路径
        System.setProperty("webdriver.http.factory", "jdk-http-client");

//        WebDriverManager.chromiumdriver().setup();
        // 设置 ChromeOptions，忽略 SSL 错误
        // 创建ChromeDriver实例对象
        ChromeDriver driver = new ChromeDriver();
        // 去模拟浏览器输入url后敲回车
        driver.get(url1);

        Thread.sleep(1000);

        // 获取body下的标签内容
         System.out.println(driver.getPageSource());
    }


}