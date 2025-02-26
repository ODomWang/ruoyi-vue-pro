package cn.iocoder.yudao.module.system.framework.captcha.config;

import cm.iocoder.captcha.service.CaptchaCacheService;
import cm.iocoder.captcha.service.impl.CaptchaServiceFactory;
import cn.iocoder.yudao.captcha.properties.AjCaptchaProperties;
import cn.iocoder.yudao.module.system.framework.captcha.core.RedisCaptchaServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 验证码的配置类
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class YudaoCaptchaConfiguration {

    @Bean
    public CaptchaCacheService captchaCacheService(AjCaptchaProperties config,
                                                   @Qualifier("stringRedisTemplate") StringRedisTemplate stringRedisTemplate) {
        CaptchaCacheService captchaCacheService = CaptchaServiceFactory.getCache(config.getCacheType().name());
        if (captchaCacheService instanceof RedisCaptchaServiceImpl) {
            ((RedisCaptchaServiceImpl) captchaCacheService).setStringRedisTemplate(stringRedisTemplate);
        }
        return captchaCacheService;
    }

}
