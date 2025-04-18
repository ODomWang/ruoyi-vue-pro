package cn.iocoder.yudao.jushauth.request;

import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.jushauth.config.AuthConfig;
import cn.iocoder.yudao.jushauth.config.AuthDefaultSource;
import cn.iocoder.yudao.jushauth.utils.UrlBuilder;

/**
 * <p>
 * 企业微信二维码登录
 * </p>
 *
 * @author yangkai.shen (https://xkcoding.com)
 * @author liguanhua (347826496(a)qq.com) 重构该类，将通用方法提取
 * @author lyadong.zhang (yadong.zhang0415(a)gmail.com) 修改类名
 * @since 1.10.0
 */
public class AuthWeChatEnterpriseQrcodeRequest extends AbstractAuthWeChatEnterpriseRequest {
    public AuthWeChatEnterpriseQrcodeRequest(AuthConfig config) {
        super(config, AuthDefaultSource.WECHAT_ENTERPRISE);
    }

    public AuthWeChatEnterpriseQrcodeRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.WECHAT_ENTERPRISE, authStateCache);
    }

    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(source.authorize())
                .queryParam("appid", config.getClientId())
                .queryParam("agentid", config.getAgentId())
                .queryParam("redirect_uri", config.getRedirectUri())
                .queryParam("state", getRealState(state))
                .build();
    }
}
