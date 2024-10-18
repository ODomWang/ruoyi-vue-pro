package cn.wenxun.admin.openai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;

import java.util.Arrays;


public class SpiderAiUtils {
    public static String SpiderByOpenAi(String htmlContent, String message, Object object,String url) {
//        Proxy proxy = Proxys.http("127.0.0.1", 7890);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-Jw1aRQcB23kMHzx3UjorQF756YTz9J4aE3yMgz9jKsm9DCEC")
//                .proxy(proxy)
                .timeout(900)
                .apiHost("https://api.302.ai") //反向代理地址
                .build()
                .init();

        Message system = Message.ofSystem(
                "你是一个网页分析师，我会给你提供网页信息，你需要分析相关内容，并且按照请求标准JSON格式返回，网站地址为 "+url+",不返回其他内容和格式信息，" +
                "标准JSON数据格式为:" + JSON.toJSONString(object, new PascalNameFilter(), SerializerFeature.WriteMapNullValue));
        Message userMessage = Message.of(message);

        Message htmlMessage = Message.ofSystem(htmlContent);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO)
                .messages(Arrays.asList(system, userMessage, htmlMessage))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        return (response.toPlainString());
    }
}
