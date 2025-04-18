package cn.iocoder.yudao.jushauth.request;

import com.alibaba.fastjson.JSONObject;
import cn.iocoder.yudao.support.HttpHeader;
import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.jushauth.config.AuthConfig;
import cn.iocoder.yudao.jushauth.config.AuthDefaultSource;
import cn.iocoder.yudao.jushauth.enums.AuthResponseStatus;
import cn.iocoder.yudao.jushauth.enums.AuthUserGender;
import cn.iocoder.yudao.jushauth.enums.scope.AuthLineScope;
import cn.iocoder.yudao.jushauth.model.AuthCallback;
import cn.iocoder.yudao.jushauth.model.AuthResponse;
import cn.iocoder.yudao.jushauth.model.AuthToken;
import cn.iocoder.yudao.jushauth.model.AuthUser;
import cn.iocoder.yudao.jushauth.utils.AuthScopeUtils;
import cn.iocoder.yudao.jushauth.utils.HttpUtils;
import cn.iocoder.yudao.jushauth.utils.UrlBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * LINE 登录, line.biz
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @since 1.16.0
 */
public class AuthLineRequest extends AuthDefaultRequest {

    public AuthLineRequest(AuthConfig config) {
        super(config, AuthDefaultSource.LINE);
    }

    public AuthLineRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.LINE, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        Map<String, String> params = new HashMap<>(8);
        params.put("grant_type", "authorization_code");
        params.put("code", authCallback.getCode());
        params.put("redirect_uri", config.getRedirectUri());
        params.put("client_id", config.getClientId());
        params.put("client_secret", config.getClientSecret());
        String response = new HttpUtils(config.getHttpConfig()).post(source.accessToken(), params, false).getBody();
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        return AuthToken.builder()
                .accessToken(accessTokenObject.getString("access_token"))
                .refreshToken(accessTokenObject.getString("refresh_token"))
                .expireIn(accessTokenObject.getIntValue("expires_in"))
                .idToken(accessTokenObject.getString("id_token"))
                .scope(accessTokenObject.getString("scope"))
                .tokenType(accessTokenObject.getString("token_type"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String userInfo = new HttpUtils(config.getHttpConfig()).get(source.userInfo(), null, new HttpHeader()
                .add("Content-Type", "application/x-www-form-urlencoded")
                .add("Authorization", "Bearer ".concat(authToken.getAccessToken())), false).getBody();
        JSONObject object = JSONObject.parseObject(userInfo);
        return AuthUser.builder()
                .rawUserInfo(object)
                .uuid(object.getString("userId"))
                .username(object.getString("displayName"))
                .nickname(object.getString("displayName"))
                .avatar(object.getString("pictureUrl"))
                .remark(object.getString("statusMessage"))
                .gender(AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
                .build();
    }

    @Override
    public AuthResponse revoke(AuthToken authToken) {
        Map<String, String> params = new HashMap<>(5);
        params.put("access_token", authToken.getAccessToken());
        params.put("client_id", config.getClientId());
        params.put("client_secret", config.getClientSecret());
        String userInfo = new HttpUtils(config.getHttpConfig()).post(source.revoke(), params, false).getBody();
        JSONObject object = JSONObject.parseObject(userInfo);
        // 返回1表示取消授权成功，否则失败
        AuthResponseStatus status = object.getBooleanValue("revoked") ? AuthResponseStatus.SUCCESS : AuthResponseStatus.FAILURE;
        return AuthResponse.builder().code(status.getCode()).msg(status.getMsg()).build();
    }

    @Override
    public AuthResponse refresh(AuthToken oldToken) {
        Map<String, String> params = new HashMap<>(8);
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", oldToken.getRefreshToken());
        params.put("client_id", config.getClientId());
        params.put("client_secret", config.getClientSecret());
        String response = new HttpUtils(config.getHttpConfig()).post(source.accessToken(), params, false).getBody();
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        return AuthResponse.builder()
                .code(AuthResponseStatus.SUCCESS.getCode())
                .data(AuthToken.builder()
                        .accessToken(accessTokenObject.getString("access_token"))
                        .refreshToken(accessTokenObject.getString("refresh_token"))
                        .expireIn(accessTokenObject.getIntValue("expires_in"))
                        .idToken(accessTokenObject.getString("id_token"))
                        .scope(accessTokenObject.getString("scope"))
                        .tokenType(accessTokenObject.getString("token_type"))
                        .build())
                .build();
    }

    @Override
    public String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo())
                .queryParam("user", authToken.getUid())
                .build();
    }

    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(super.authorize(state))
                .queryParam("nonce", state)
                .queryParam("scope", this.getScopes(" ", true, AuthScopeUtils.getDefaultScopes(AuthLineScope.values())))
                .build();
    }
}
