package cn.iocoder.yudao.justauth.autoconfigure;

import cn.iocoder.yudao.HttpUtil;
import cn.iocoder.yudao.support.hutool.HutoolImpl;
import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * 社交自动装配类
 * </p>
 *
 * @author xingyu
 * @date 2023-09-11 10:52
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(JustAuthProperties.class)
public class SocialAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "justauth", value = "enabled", havingValue = "true", matchIfMissing = true)
    public AuthRequestFactory yudaoAuthRequestFactory(JustAuthProperties properties, AuthStateCache authStateCache) {
        // 需要修改 HttpUtil 使用的实现，避免类报错
        HttpUtil.setHttp(new HutoolImpl());
        // 创建 YudaoAuthRequestFactory
        return new AuthRequestFactory(properties, authStateCache);
    }
}
