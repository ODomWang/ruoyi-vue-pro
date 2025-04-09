package cn.iocoder.yudao.module.wenxun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wenxun.api")
@Data
public class WenxunConfig {

    /**
     * API基础URL
     */
    private String baseUrl = "http://110.40.228.6:2531/v2/api";

    /**
     * API Token
     */
    private String token;

    /**
     * 应用ID
     */
    private String appId;

} 