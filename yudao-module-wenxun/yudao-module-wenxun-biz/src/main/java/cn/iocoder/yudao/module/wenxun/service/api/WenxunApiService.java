package cn.iocoder.yudao.module.wenxun.service.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.vo.*;

/**
 * 文巡 API Service 接口
 */
public interface WenxunApiService {

    /**
     * 获取 token
     *
     * @return token
     */
    CommonResult<String> getToken();

    /**
     * 获取登录二维码
     *
     * @param appId 应用ID
     * @return 二维码信息
     */
    CommonResult<LoginQrCodeRespVO> getLoginQrCode(String appId);

    /**
     * 检查登录状态
     *
     * @param appId 应用ID
     * @param captchCode 验证码(可选)
     * @return 登录状态
     */
    CommonResult<LoginStatusRespVO> checkLoginStatus(String appId, String captchCode, String uuid);

    /**
     * 检查登录状态
     *
     * @param appId 应用ID
     * @return 登录状态
     */
    default CommonResult<LoginStatusRespVO> checkLoginStatus(String appId, String uuid) {
        return checkLoginStatus(appId, null,uuid);
    }
    
    /**
     * 获取联系人列表
     *
     * @param wxId 微信ID
     * @return 联系人列表
     */
    CommonResult<ContactListRespVO> getContactList(String wxId);
    
    /**
     * 获取联系人列表（包含appId）
     *
     * @param wxId 微信ID
     * @param appId 应用ID
     * @param refresh 是否刷新缓存，true表示从接口重新获取，false表示优先从数据库获取
     * @return 联系人列表
     */
    CommonResult<ContactListRespVO> getContactList(String wxId, String appId, boolean refresh);
    
    /**
     * 获取联系人详情
     *
     * @param wxId 微信ID
     * @param friendWxId 好友微信ID
     * @return 联系人详情
     */
    CommonResult<ContactDetailRespVO> getContactDetail(String wxId, String friendWxId);
    
    /**
     * 获取联系人详情（包含appId）
     *
     * @param wxId 微信ID
     * @param friendWxId 好友微信ID
     * @param appId 应用ID
     * @param refresh 是否刷新缓存，true表示从接口重新获取，false表示优先从数据库获取
     * @return 联系人详情
     */
    CommonResult<ContactDetailRespVO> getContactDetail(String wxId, String friendWxId, String appId, boolean refresh);
    
    /**
     * 更新联系人信息
     *
     * @param wxId 微信ID
     * @param reqVO 更新请求
     * @return 更新结果
     */
    CommonResult<Boolean> updateContact(String wxId, ContactUpdateReqVO reqVO);
    
    /**
     * 更新联系人信息（包含appId）
     *
     * @param wxId 微信ID
     * @param reqVO 更新请求
     * @param appId 应用ID
     * @return 更新结果
     */
    CommonResult<Boolean> updateContact(String wxId, ContactUpdateReqVO reqVO, String appId);
} 