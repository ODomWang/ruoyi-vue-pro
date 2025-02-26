package cn.iocoder.yudao.framework.ai.chat;

import cn.iocoder.yudao.framework.ai.core.model.deepseek.DeepSeekChatModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeepSeekChatModel} 集成测试
 *
 * @author 芋道源码
 */
public class DeepSeekChatModelTests {

    private final DeepSeekChatModel chatModel = new DeepSeekChatModel("sk-037634bbdc744b958fcac71703145891");

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
    }

    @Test
    @Disabled
    public void testStream() {
        // 准备参数
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("你是一个优质的文言文作者，用文言文描述着各城市的人文风景。"));
        messages.add(new UserMessage("你是谁"));
        ChatOptions chatOptions =
                OpenAiChatOptions.builder().withModel("deepseek-reasoner").withMaxTokens(4097).build();

        // 调用
        Flux<ChatResponse> flux = chatModel.stream(new Prompt(messages,chatOptions));

         // 打印结果
        flux.doOnNext(System.out::println).then().block();
    }

}
