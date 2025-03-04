package cn.iocoder.yudao.jushauth.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.iocoder.yudao.support.HttpHeader;
import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.jushauth.config.AuthConfig;
import cn.iocoder.yudao.jushauth.config.AuthDefaultSource;
import cn.iocoder.yudao.jushauth.enums.AuthResponseStatus;
import cn.iocoder.yudao.jushauth.enums.AuthUserGender;
import cn.iocoder.yudao.jushauth.enums.scope.AuthSlackScope;
import cn.iocoder.yudao.jushauth.exception.AuthException;
import cn.iocoder.yudao.jushauth.model.AuthCallback;
import cn.iocoder.yudao.jushauth.model.AuthResponse;
import cn.iocoder.yudao.jushauth.model.AuthToken;
import cn.iocoder.yudao.jushauth.model.AuthUser;
import cn.iocoder.yudao.jushauth.utils.AuthScopeUtils;
import cn.iocoder.yudao.jushauth.utils.HttpUtils;
import cn.iocoder.yudao.jushauth.utils.UrlBuilder;

/**
 * slack登录, slack.com
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @since 1.16.0
 */
public class AuthSlackRequest extends AuthDefaultRequest {

    public AuthSlackRequest(AuthConfig config) {
        super(config, AuthDefaultSource.SLACK);
    }

    public AuthSlackRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.SLACK, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        HttpHeader header = new HttpHeader()
                .add("Content-Type", "application/x-www-form-urlencoded");
        String response = new HttpUtils(config.getHttpConfig())
                .get(accessTokenUrl(authCallback.getCode()), null, header, false).getBody();
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        this.checkResponse(accessTokenObject);
        return AuthToken.builder()
                .accessToken(accessTokenObject.getString("access_token"))
                .scope(accessTokenObject.getString("scope"))
                .tokenType(accessTokenObject.getString("token_type"))
                .uid(accessTokenObject.getJSONObject("authed_user").getString("id"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        HttpHeader header = new HttpHeader()
                .add("Content-Type", "application/x-www-form-urlencoded")
                .add("Authorization", "Bearer ".concat(authToken.getAccessToken()));
        String userInfo = new HttpUtils(config.getHttpConfig())
                .get(userInfoUrl(authToken), null, header, false).getBody();
        JSONObject object = JSONObject.parseObject(userInfo);
        this.checkResponse(object);
        JSONObject user = object.getJSONObject("user");
        JSONObject profile = user.getJSONObject("profile");
        return AuthUser.builder()
                .rawUserInfo(user)
                .uuid(user.getString("id"))
                .username(user.getString("name"))
                .nickname(user.getString("real_name"))
                .avatar(profile.getString("image_original"))
                .email(profile.getString("email"))
                .gender(AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
                .build();
    }

    @Override
    public AuthResponse revoke(AuthToken authToken) {
        HttpHeader header = new HttpHeader()
                .add("Content-Type", "application/x-www-form-urlencoded")
                .add("Authorization", "Bearer ".concat(authToken.getAccessToken()));
        String userInfo = new HttpUtils(config.getHttpConfig())
                .get(source.revoke(), null, header, false).getBody();
        JSONObject object = JSONObject.parseObject(userInfo);
        this.checkResponse(object);
        // 返回1表示取消授权成功，否则失败
        AuthResponseStatus status = object.getBooleanValue("revoked") ? AuthResponseStatus.SUCCESS : AuthResponseStatus.FAILURE;
        return AuthResponse.builder().code(status.getCode()).msg(status.getMsg()).build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (!object.getBooleanValue("ok")) {
            String errorMsg = object.getString("error");
            if (object.containsKey("response_metadata")) {
                JSONArray array = object.getJSONObject("response_metadata").getJSONArray("messages");
                if (null != array && array.size() > 0) {
                    errorMsg += "; " + String.join(",", array.toArray(new String[0]));
                }
            }

            throw new AuthException(errorMsg);
        }
    }

    @Override
    public String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo())
                .queryParam("user", authToken.getUid())
                .build();
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(source.authorize())
                .queryParam("client_id", config.getClientId())
                .queryParam("state", getRealState(state))
                .queryParam("redirect_uri", config.getRedirectUri())
                .queryParam("scope", this.getScopes(",", true, AuthScopeUtils.getDefaultScopes(AuthSlackScope.values())))
                .build();
    }

    @Override
    protected String accessTokenUrl(String code) {
        return UrlBuilder.fromBaseUrl(source.accessToken())
                .queryParam("code", code)
                .queryParam("client_id", config.getClientId())
                .queryParam("client_secret", config.getClientSecret())
                .queryParam("redirect_uri", config.getRedirectUri())
                .build();
    }
}
