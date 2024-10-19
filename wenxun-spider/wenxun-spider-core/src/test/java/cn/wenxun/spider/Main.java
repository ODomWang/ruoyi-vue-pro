package cn.wenxun.spider;


import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
        String url1 = "http://www.wxcu.edu.cn/static/newList.html?cid=19";
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

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");

        WebDriver driver = new ChromeDriver(options);
        driver.get(url1);
        ((ChromeDriver) driver).executeScript("if (window.onload) window.onload();");

        Thread.sleep(1000);

        // 获取body下的标签内容
         System.out.println(driver.getPageSource());
    }


}