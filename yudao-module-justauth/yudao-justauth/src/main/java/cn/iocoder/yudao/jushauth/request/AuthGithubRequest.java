package cn.iocoder.yudao.jushauth.request;

import com.alibaba.fastjson.JSONObject;
import cn.iocoder.yudao.support.HttpHeader;
import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.jushauth.config.AuthConfig;
import cn.iocoder.yudao.jushauth.config.AuthDefaultSource;
import cn.iocoder.yudao.jushauth.enums.AuthUserGender;
import cn.iocoder.yudao.jushauth.enums.scope.AuthGithubScope;
import cn.iocoder.yudao.jushauth.exception.AuthException;
import cn.iocoder.yudao.jushauth.model.AuthCallback;
import cn.iocoder.yudao.jushauth.model.AuthToken;
import cn.iocoder.yudao.jushauth.model.AuthUser;
import cn.iocoder.yudao.jushauth.utils.AuthScopeUtils;
import cn.iocoder.yudao.jushauth.utils.GlobalAuthUtils;
import cn.iocoder.yudao.jushauth.utils.HttpUtils;
import cn.iocoder.yudao.jushauth.utils.UrlBuilder;

import java.util.Map;

/**
 * Github登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @since 1.0.0
 */
public class AuthGithubRequest extends AuthDefaultRequest {

    public AuthGithubRequest(AuthConfig config) {
        super(config, AuthDefaultSource.GITHUB);
    }

    public AuthGithubRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.GITHUB, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        Map<String, String> res = GlobalAuthUtils.parseStringToMap(response);

        this.checkResponse(res.containsKey("error"), res.get("error_description"));

        return AuthToken.builder()
                .accessToken(res.get("access_token"))
                .scope(res.get("scope"))
                .tokenType(res.get("token_type"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        HttpHeader header = new HttpHeader();
        header.add("Authorization", "token " + authToken.getAccessToken());
        String response = new HttpUtils(config.getHttpConfig()).get(UrlBuilder.fromBaseUrl(source.userInfo()).build(), null, header, false).getBody();
        JSONObject object = JSONObject.parseObject(response);

        this.checkResponse(object.containsKey("error"), object.getString("error_description"));

        return AuthUser.builder()
                .rawUserInfo(object)
                .uuid(object.getString("id"))
                .username(object.getString("login"))
                .avatar(object.getString("avatar_url"))
                .blog(object.getString("blog"))
                .nickname(object.getString("name"))
                .company(object.getString("company"))
                .location(object.getString("location"))
                .email(object.getString("email"))
                .remark(object.getString("bio"))
                .gender(AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
                .build();
    }

    private void checkResponse(boolean error, String errorDescription) {
        if (error) {
            throw new AuthException(errorDescription);
        }
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(super.authorize(state))
                .queryParam("scope", this.getScopes(" ", true, AuthScopeUtils.getDefaultScopes(AuthGithubScope.values())))
                .build();
    }

}
