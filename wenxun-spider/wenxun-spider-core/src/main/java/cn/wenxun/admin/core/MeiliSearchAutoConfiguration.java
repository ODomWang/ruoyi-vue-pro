package cn.wenxun.admin.core;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class MeiliSearchAutoConfiguration {

    @Value("${meilisearch.hostUrl}")
    private String hostUrl;
    @Value("${meilisearch.apiKey}")
    private String apiKey;

    @Bean
    @ConditionalOnMissingBean(Client.class)
    Client client() {
        return new Client(config());
    }

    @Bean
    @ConditionalOnMissingBean(Config.class)
    Config config() {
        return new Config(hostUrl, apiKey);
    }
}