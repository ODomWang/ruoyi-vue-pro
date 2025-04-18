package cn.iocoder.yudao.framework.ai.chat;

import cn.iocoder.yudao.framework.ai.core.util.AiUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link OpenAiChatModel} 集成测试
 *
 * @author 芋道源码
 */
public class OpenAIChatModelTests {

    private final OpenAiApi openAiApi = new OpenAiApi(
            "https://api.302.ai",
            "sk-Jw1aRQcB23kMHzx3UjorQF756YTz9J4aE3yMgz9jKsm9DCEC");
    private final OpenAiChatModel chatModel = new OpenAiChatModel(openAiApi,
            OpenAiChatOptions.builder().withModel(OpenAiApi.ChatModel.GPT_4_O).build());

    @Test
    @Disabled
    public void testCall() {
        // 准备参数
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("你是一个优质的文言文作者，用文言文描述着各城市的人文风景。"));
        messages.add(new UserMessage("1 + 1 = ？"));

        // 调用
        ChatResponse response = chatModel.call(new Prompt(messages));
        // 打印结果
        System.out.println(response);
        System.out.println(response.getResult().getOutput());
    }

    @Test
    @Disabled
    public void testStream() {
        // 准备参数
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("你是一个厌恶我的女朋友，请用她的口味说话"));
        messages.add(new UserMessage("1 + 1 = ？"));
        ChatOptions chatOptions =
          OpenAiChatOptions.builder().withModel("gpt-3.5-turbo").withMaxTokens(4097).build();

        // 调用
        Flux<ChatResponse> flux = chatModel.stream(new Prompt(messages,chatOptions));
        // 打印结果
        flux.doOnNext(response -> {
//            System.out.println(response);
            try {

                System.out.println(response.getResult().getOutput().getContent());
            }catch (Exception e){}
        }).then().block();
    }

}
