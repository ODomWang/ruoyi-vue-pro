package cn.iocoder.yudao.jushauth.request;

import com.alibaba.fastjson.JSONObject;
import cn.iocoder.yudao.constants.Constants;
import cn.iocoder.yudao.support.HttpHeader;
import cn.iocoder.yudao.util.MapUtil;
import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.jushauth.config.AuthConfig;
import cn.iocoder.yudao.jushauth.enums.AuthUserGender;
import cn.iocoder.yudao.jushauth.enums.scope.AuthStackoverflowScope;
import cn.iocoder.yudao.jushauth.exception.AuthException;
import cn.iocoder.yudao.jushauth.model.AuthCallback;
import cn.iocoder.yudao.jushauth.model.AuthToken;
import cn.iocoder.yudao.jushauth.model.AuthUser;
import cn.iocoder.yudao.jushauth.utils.AuthScopeUtils;
import cn.iocoder.yudao.jushauth.utils.HttpUtils;
import cn.iocoder.yudao.jushauth.utils.UrlBuilder;

import java.util.Map;

import static cn.iocoder.yudao.jushauth.config.AuthDefaultSource.STACK_OVERFLOW;

/**
 * Stack Overflow登录
 *
 * @author hongwei.peng (pengisgood(at)gmail(dot)com)
 * @since 1.9.0
 */
public class AuthStackOverflowRequest extends AuthDefaultRequest {

    public AuthStackOverflowRequest(AuthConfig config) {
        super(config, STACK_OVERFLOW);
    }

    public AuthStackOverflowRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, STACK_OVERFLOW, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String accessTokenUrl = accessTokenUrl(authCallback.getCode());
        Map<String, String> form = MapUtil.parseStringToMap(accessTokenUrl, false);
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add(Constants.CONTENT_TYPE, "application/x-www-form-urlencoded");
        String response = new HttpUtils(config.getHttpConfig()).post(accessTokenUrl, form, httpHeader, false).getBody();

        JSONObject accessTokenObject = JSONObject.parseObject(response);
        this.checkResponse(accessTokenObject);

        return AuthToken.builder()
                .accessToken(accessTokenObject.getString("access_token"))
                .expireIn(accessTokenObject.getIntValue("expires"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String userInfoUrl = UrlBuilder.fromBaseUrl(this.source.userInfo())
                .queryParam("access_token", authToken.getAccessToken())
                .queryParam("site", "stackoverflow")
                .queryParam("key", this.config.getStackOverflowKey())
                .build();
        String response = new HttpUtils(config.getHttpConfig()).get(userInfoUrl).getBody();
        JSONObject object = JSONObject.parseObject(response);
        this.checkResponse(object);
        JSONObject userObj = object.getJSONArray("items").getJSONObject(0);

        return AuthUser.builder()
                .rawUserInfo(userObj)
                .uuid(userObj.getString("user_id"))
                .avatar(userObj.getString("profile_image"))
                .location(userObj.getString("location"))
                .nickname(userObj.getString("display_name"))
                .blog(userObj.getString("website_url"))
                .gender(AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
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
        return UrlBuilder.fromBaseUrl(super.authorize(state))
                .queryParam("scope", this.getScopes(",", false, AuthScopeUtils.getDefaultScopes(AuthStackoverflowScope.values())))
                .build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
    }
}
