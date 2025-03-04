package cn.iocoder.yudao.captcha.config;

import cm.iocoder.captcha.service.CaptchaCacheService;
import cm.iocoder.captcha.service.impl.CaptchaServiceFactory;
import cn.iocoder.yudao.captcha.properties.AjCaptchaProperties;
 import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 存储策略自动配置.
 */
@Configuration
public class AjCaptchaStorageAutoConfiguration {

    @Bean(name = "AjCaptchaCacheService")
    @ConditionalOnMissingBean
    public CaptchaCacheService captchaCacheService(AjCaptchaProperties config) {
        //缓存类型redis/local/....
        return CaptchaServiceFactory.getCache(config.getCacheType().name());
    }
}
