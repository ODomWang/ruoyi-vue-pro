package cn.iocoder.yudao.jushauth.request;

import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.jushauth.config.AuthConfig;
import cn.iocoder.yudao.jushauth.config.AuthDefaultSource;

/**
 * 微软中国登录(世纪华联)
 *
 * @author mroldx (xzfqq5201314@gmail.com)
 * @since 1.16.4
 */
public class AuthMicrosoftCnRequest extends AbstractAuthMicrosoftRequest {

    public AuthMicrosoftCnRequest(AuthConfig config) {
        super(config, AuthDefaultSource.MICROSOFT_CN);
    }

    public AuthMicrosoftCnRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.MICROSOFT_CN, authStateCache);
    }

}
