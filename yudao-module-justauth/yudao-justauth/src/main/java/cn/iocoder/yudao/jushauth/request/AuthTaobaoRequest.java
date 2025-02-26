package cn.iocoder.yudao.jushauth.request;

import com.alibaba.fastjson.JSONObject;
import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.jushauth.config.AuthConfig;
import cn.iocoder.yudao.jushauth.config.AuthDefaultSource;
import cn.iocoder.yudao.jushauth.enums.AuthResponseStatus;
import cn.iocoder.yudao.jushauth.enums.AuthUserGender;
import cn.iocoder.yudao.jushauth.exception.AuthException;
import cn.iocoder.yudao.jushauth.model.AuthCallback;
import cn.iocoder.yudao.jushauth.model.AuthResponse;
import cn.iocoder.yudao.jushauth.model.AuthToken;
import cn.iocoder.yudao.jushauth.model.AuthUser;
import cn.iocoder.yudao.jushauth.utils.GlobalAuthUtils;
import cn.iocoder.yudao.jushauth.utils.HttpUtils;
import cn.iocoder.yudao.jushauth.utils.StringUtils;
import cn.iocoder.yudao.jushauth.utils.UrlBuilder;

/**
 * 淘宝登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @since 1.1.0
 */
public class AuthTaobaoRequest extends AuthDefaultRequest {

    public AuthTaobaoRequest(AuthConfig config) {
        super(config, AuthDefaultSource.TAOBAO);
    }

    public AuthTaobaoRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.TAOBAO, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        return AuthToken.builder().accessCode(authCallback.getCode()).build();
    }

    private AuthToken getAuthToken(JSONObject object) {
        this.checkResponse(object);

        return AuthToken.builder()
                .accessToken(object.getString("access_token"))
                .expireIn(object.getIntValue("expires_in"))
                .tokenType(object.getString("token_type"))
                .idToken(object.getString("id_token"))
                .refreshToken(object.getString("refresh_token"))
                .uid(object.getString("taobao_user_id"))
                .openId(object.getString("taobao_open_uid"))
                .build();
    }

    private void checkResponse(JSONObject object) {
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String response = doPostAuthorizationCode(authToken.getAccessCode());
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        if (accessTokenObject.containsKey("error")) {
            throw new AuthException(accessTokenObject.getString("error_description"));
        }
        authToken = this.getAuthToken(accessTokenObject);

        String nick = GlobalAuthUtils.urlDecode(accessTokenObject.getString("taobao_user_nick"));
        return AuthUser.builder()
                .rawUserInfo(accessTokenObject)
                .uuid(StringUtils.isEmpty(authToken.getUid()) ? authToken.getOpenId() : authToken.getUid())
                .username(nick)
                .nickname(nick)
                .gender(AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
                .build();
    }

    @Override
    public AuthResponse refresh(AuthToken oldToken) {
        String tokenUrl = refreshTokenUrl(oldToken.getRefreshToken());
        String response = new HttpUtils(config.getHttpConfig()).post(tokenUrl).getBody();
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        return AuthResponse.builder()
                .code(AuthResponseStatus.SUCCESS.getCode())
                .data(this.getAuthToken(accessTokenObject))
                .build();
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     * @since 1.9.3
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(source.authorize())
                .queryParam("response_type", "code")
                .queryParam("client_id", config.getClientId())
                .queryParam("redirect_uri", config.getRedirectUri())
                .queryParam("view", "web")
                .queryParam("state", getRealState(state))
                .build();
    }
}
