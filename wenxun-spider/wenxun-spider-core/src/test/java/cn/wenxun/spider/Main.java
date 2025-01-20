package cn.wenxun.spider;


import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        System.out.println(test("http://dangjian.people.com.cn/GB/394443/index1.html"));

        System.out.println(test("http://dangjian.people.com.cn/GB/394443/index2.html"));
    }

     public static String test(String url)  {
         try {
             // 检查目标 URL 是否有效
             URI targetUri = new URI(url);

             // 使用 RestTemplate 获取目标内容
             RestTemplate restTemplate = new RestTemplate();
             ResponseEntity<byte[]> response = restTemplate.getForEntity(targetUri, byte[].class);

             // 动态获取编码
             String contentType = response.getHeaders().getContentType().toString();
             Charset charset = StandardCharsets.UTF_8;
             if (contentType.contains("charset=")) {
                 String detectedCharset = contentType.substring(contentType.indexOf("charset=") + 8);
                 charset = Charset.forName(detectedCharset);

             }


             // 解码内容
             String responseBody = new String(response.getBody(), charset);
             // 返回修改后的响应
             return responseBody;

         } catch (URISyntaxException e) {
             return null
                     ;
         } catch (Exception e) {
             return null;
         }
    }

}