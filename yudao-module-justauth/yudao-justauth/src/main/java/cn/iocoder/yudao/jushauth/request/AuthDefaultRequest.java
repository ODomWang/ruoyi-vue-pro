package cn.iocoder.yudao.jushauth.request;

import com.alibaba.fastjson.JSON;
import cn.iocoder.yudao.util.UrlUtil;
import cn.iocoder.yudao.jushauth.cache.AuthDefaultStateCache;
import cn.iocoder.yudao.jushauth.cache.AuthStateCache;
import cn.iocoder.yudao.jushauth.config.AuthConfig;
import cn.iocoder.yudao.jushauth.config.AuthSource;
import cn.iocoder.yudao.jushauth.enums.AuthResponseStatus;
import cn.iocoder.yudao.jushauth.exception.AuthException;
import cn.iocoder.yudao.jushauth.log.Log;
import cn.iocoder.yudao.jushauth.model.AuthCallback;
import cn.iocoder.yudao.jushauth.model.AuthResponse;
import cn.iocoder.yudao.jushauth.model.AuthToken;
import cn.iocoder.yudao.jushauth.model.AuthUser;
import cn.iocoder.yudao.jushauth.utils.*;

import java.util.List;

/**
 * 默认的request处理类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @author yangkai.shen (https://xkcoding.com)
 * @since 1.0.0
 */
public abstract class AuthDefaultRequest implements AuthRequest {
    protected AuthConfig config;
    protected AuthSource source;
    protected AuthStateCache authStateCache;

    public AuthDefaultRequest(AuthConfig config, AuthSource source) {
        this(config, source, AuthDefaultStateCache.INSTANCE);
    }

    public AuthDefaultRequest(AuthConfig config, AuthSource source, AuthStateCache authStateCache) {
        this.config = config;
        this.source = source;
        this.authStateCache = authStateCache;
        if (!AuthChecker.isSupportedAuth(config, source)) {
            throw new AuthException(AuthResponseStatus.PARAMETER_INCOMPLETE, source);
        }
        // 校验配置合法性
        AuthChecker.checkConfig(config, source);
    }

    /**
     * 获取access token
     *
     * @param authCallback 授权成功后的回调参数
     * @return token
     * @see AuthDefaultRequest#authorize()
     * @see AuthDefaultRequest#authorize(String)
     */
    protected abstract AuthToken getAccessToken(AuthCallback authCallback);

    /**
     * 使用token换取用户信息
     *
     * @param authToken token信息
     * @return 用户信息
     * @see AuthDefaultRequest#getAccessToken(AuthCallback)
     */
    protected abstract AuthUser getUserInfo(AuthToken authToken);

    @Override
    public AuthResponse authToken(AuthCallback authCallback) {
        try {
            checkCode(authCallback);
            if (!config.isIgnoreCheckState()) {
                AuthChecker.checkState(authCallback.getState(), source, authStateCache);
            }
            return AuthResponse.builder().code(AuthResponseStatus.SUCCESS.getCode()).data(this.getAccessToken(authCallback)).build();
        } catch (Exception e) {
            Log.error("Failed get auth token.", e);
            return this.responseError(e);
        }
    }

    @Override
    public AuthResponse userInfo(AuthToken authToken) {
        try {
            //先刷新token，再获取信息
            //this.refresh(authToken);
            AuthUser user = this.getUserInfo(authToken);
            return AuthResponse.builder().code(AuthResponseStatus.SUCCESS.getCode()).data(user).build();
        } catch (Exception e) {
            Log.error("Failed get userInfo.", e);
            return this.responseError(e);
        }
    }

    /**
     * 统一的登录入口。当通过{@link AuthDefaultRequest#authorize(String)}授权成功后，会跳转到调用方的相关回调方法中
     * 方法的入参可以使用{@code AuthCallback}，{@code AuthCallback}类中封装好了OAuth2授权回调所需要的参数
     *
     * @param authCallback 用于接收回调参数的实体
     * @return AuthResponse
     */
    @Override
    public AuthResponse login(AuthCallback authCallback) {
        try {
            checkCode(authCallback);
            if (!config.isIgnoreCheckState()) {
                AuthChecker.checkState(authCallback.getState(), source, authStateCache);
            }

            AuthToken authToken = this.getAccessToken(authCallback);
            AuthUser user = this.getUserInfo(authToken);
            return AuthResponse.builder().code(AuthResponseStatus.SUCCESS.getCode()).data(user).build();
        } catch (Exception e) {
            Log.error("Failed to login with oauth authorization.", e);
            return this.responseError(e);
        }
    }

    protected void checkCode(AuthCallback authCallback) {
        AuthChecker.checkCode(source, authCallback);
    }

    /**
     * 处理{@link AuthDefaultRequest#login(AuthCallback)} 发生异常的情况，统一响应参数
     *
     * @param e 具体的异常
     * @return AuthResponse
     */
    AuthResponse responseError(Exception e) {
        int errorCode = AuthResponseStatus.FAILURE.getCode();
        String errorMsg = e.getMessage();
        if (e instanceof AuthException) {
            AuthException authException = ((AuthException) e);
            errorCode = authException.getErrorCode();
            if (StringUtils.isNotEmpty(authException.getErrorMsg())) {
                errorMsg = authException.getErrorMsg();
            }
        }
        return AuthResponse.builder().code(errorCode).msg(errorMsg).build();
    }

    /**
     * 返回授权url，可自行跳转页面
     * <p>
     * 不建议使用该方式获取授权地址，不带{@code state}的授权地址，容易受到csrf攻击。
     * 建议使用{@link AuthDefaultRequest#authorize(String)}方法生成授权地址，在回调方法中对{@code state}进行校验
     *
     * @return 返回授权地址
     * @see AuthDefaultRequest#authorize(String)
     */
    @Deprecated
    @Override
    public String authorize() {
        return this.authorize(null);
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
                .queryParam("state", getRealState(state))
                .build();
    }

    /**
     * 返回获取accessToken的url
     *
     * @param code 授权码
     * @return 返回获取accessToken的url
     */
    protected String accessTokenUrl(String code) {
        return UrlBuilder.fromBaseUrl(source.accessToken())
                .queryParam("code", code)
                .queryParam("client_id", config.getClientId())
                .queryParam("client_secret", config.getClientSecret())
                .queryParam("grant_type", "authorization_code")
                .queryParam("redirect_uri", config.getRedirectUri())
                .build();
    }

    /**
     * 返回获取accessToken的url
     *
     * @param refreshToken refreshToken
     * @return 返回获取accessToken的url
     */
    protected String refreshTokenUrl(String refreshToken) {
        return UrlBuilder.fromBaseUrl(source.refresh())
                .queryParam("client_id", config.getClientId())
                .queryParam("client_secret", config.getClientSecret())
                .queryParam("refresh_token", refreshToken)
                .queryParam("grant_type", "refresh_token")
                .queryParam("redirect_uri", config.getRedirectUri())
                .build();
    }

    /**
     * 返回获取userInfo的url
     *
     * @param authToken token
     * @return 返回获取userInfo的url
     */
    protected String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo()).queryParam("access_token", authToken.getAccessToken()).build();
    }

    /**
     * 返回获取revoke authorization的url
     *
     * @param authToken token
     * @return 返回获取revoke authorization的url
     */
    protected String revokeUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.revoke()).queryParam("access_token", authToken.getAccessToken()).build();
    }

    /**
     * 获取state，如果为空， 则默认取当前日期的时间戳
     *
     * @param state 原始的state
     * @return 返回不为null的state
     */
    protected String getRealState(String state) {
        if (StringUtils.isEmpty(state)) {
            state = UuidUtils.getUUID();
        }
        // 缓存state
        authStateCache.cache(state, state);
        return state;
    }

    /**
     * 通用的 authorizationCode 协议
     *
     * @param code code码
     * @return Response
     */
    protected String doPostAuthorizationCode(String code) {
        return new HttpUtils(config.getHttpConfig()).post(accessTokenUrl(code)).getBody();
    }

    /**
     * 通用的 authorizationCode 协议
     *
     * @param code code码
     * @return Response
     */
    protected String doGetAuthorizationCode(String code) {
        return new HttpUtils(config.getHttpConfig()).get(accessTokenUrl(code)).getBody();
    }

    /**
     * 通用的 用户信息
     *
     * @param authToken token封装
     * @return Response
     */
    @Deprecated
    protected String doPostUserInfo(AuthToken authToken) {
        return new HttpUtils(config.getHttpConfig()).post(userInfoUrl(authToken)).getBody();
    }

    /**
     * 通用的 用户信息
     *
     * @param authToken token封装
     * @return Response
     */
    protected String doGetUserInfo(AuthToken authToken) {
        return new HttpUtils(config.getHttpConfig()).get(userInfoUrl(authToken)).getBody();
    }

    /**
     * 通用的post形式的取消授权方法
     *
     * @param authToken token封装
     * @return Response
     */
    @Deprecated
    protected String doPostRevoke(AuthToken authToken) {
        return new HttpUtils(config.getHttpConfig()).post(revokeUrl(authToken)).getBody();
    }

    /**
     * 通用的post形式的取消授权方法
     *
     * @param authToken token封装
     * @return Response
     */
    protected String doGetRevoke(AuthToken authToken) {
        return new HttpUtils(config.getHttpConfig()).get(revokeUrl(authToken)).getBody();
    }

    /**
     * 获取以 {@code separator}分割过后的 scope 信息
     *
     * @param separator     多个 {@code scope} 间的分隔符
     * @param encode        是否 encode 编码
     * @param defaultScopes 默认的 scope， 当客户端没有配置 {@code scopes} 时启用
     * @return String
     * @since 1.16.7
     */
    protected String getScopes(String separator, boolean encode, List<String> defaultScopes) {
        List<String> scopes = config.getScopes();
        if (null == scopes || scopes.isEmpty()) {
            if (null == defaultScopes || defaultScopes.isEmpty()) {
                return "";
            }
            scopes = defaultScopes;
        }
        if (null == separator) {
            // 默认为空格
            separator = " ";
        }
        String scopeStr = String.join(separator, scopes);
        return encode ? UrlUtil.urlEncode(scopeStr) : scopeStr;
    }

    /**
     * 获取一个AuthConfig的拷贝
     *
     * @return AuthConfig
     */
    public AuthConfig copyConfig() {
        if (config == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(config), AuthConfig.class);
    }

}
