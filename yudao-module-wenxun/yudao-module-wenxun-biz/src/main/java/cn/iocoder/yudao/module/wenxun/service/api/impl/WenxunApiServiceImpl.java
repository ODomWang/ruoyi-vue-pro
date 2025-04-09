package cn.iocoder.yudao.module.wenxun.service.api.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wenxun.config.WenxunConfig;
import cn.iocoder.yudao.module.wenxun.controller.admin.vo.*;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WxAccountDO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WxContactDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.WxAccountMapper;
import cn.iocoder.yudao.module.wenxun.dal.mysql.WxContactMapper;
import cn.iocoder.yudao.module.wenxun.service.api.WenxunApiService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Service
@Slf4j
@Validated
public class WenxunApiServiceImpl implements WenxunApiService {

    @Resource
    private WenxunConfig wenxunConfig;

    @Resource
    private WxAccountMapper wxAccountMapper;
    
    @Resource
    private WxContactMapper wxContactMapper;

    @Value("${wenxun.api.base-url}")
    private String apiBaseUrl;

    // 缓存token,避免重复获取
    private String cachedToken;
    private Long tokenExpireTime;

    private synchronized String getOrRefreshToken() {
        // 如果token为空或已过期,重新获取
        if (cachedToken == null || System.currentTimeMillis() >= tokenExpireTime) {
            try {
                HttpResponse response = HttpUtil.createPost(apiBaseUrl + "/tools/getTokenId")
                        .execute();

                if (response.isOk()) {
                    JSONObject jsonResponse = JSON.parseObject(response.body());
                    if (jsonResponse.getInteger("ret") == 200) {
                        cachedToken = jsonResponse.getString("data");
                        // 设置token过期时间为23小时(保守估计,避免临界点)
                        tokenExpireTime = System.currentTimeMillis() + 23 * 60 * 60 * 1000;
                    } else {
                        log.error("获取token失败: {}", jsonResponse.getString("msg"));
                        return null;
                    }
                } else {
                    log.error("获取token请求失败: {}", response.getStatus());
                    return null;
                }
            } catch (Exception e) {
                log.error("获取token异常", e);
                return null;
            }
        }
        return cachedToken;
    }

    @Override
    public CommonResult<LoginQrCodeRespVO> getLoginQrCode(String appId) {
        try {
            String token = getOrRefreshToken();
            if (token == null) {
                return error(500, "获取token失败");
            }

            JSONObject params = new JSONObject();
            params.put("appId", appId);

            HttpResponse response = HttpUtil.createPost(apiBaseUrl + "/login/getLoginQrCode")
                    .header("Content-Type", "application/json")
                    .header("X-GEWE-TOKEN", token)
                    .body(params.toJSONString())
                    .execute();

            if (!response.isOk()) {
                return error(response.getStatus(), "请求失败");
            }

            JSONObject jsonResponse = JSON.parseObject(response.body());
            if (jsonResponse.getInteger("ret") != 200) {
                return error(jsonResponse.getInteger("ret"), jsonResponse.getString("msg"));
            }

            LoginQrCodeRespVO respVO = new LoginQrCodeRespVO();
            JSONObject data = jsonResponse.getJSONObject("data");
            respVO.setQrImgBase64(data.getString("qrImgBase64"));
            respVO.setQrData(data.getString("qrData"));
            respVO.setAppId(data.getString("appId"));
            return success(respVO);
        } catch (Exception e) {
            return error(500, "系统异常：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public CommonResult<LoginStatusRespVO> checkLoginStatus(String appId, String captchCode, String uuid) {
        try {
            String token = getOrRefreshToken();
            if (token == null) {
                return error(500, "获取token失败");
            }

            JSONObject params = new JSONObject();
            params.put("appId", appId);
            params.put("uuid", uuid);
            // 如果有验证码则加入请求参数
            if (captchCode != null && !captchCode.isEmpty()) {
                params.put("captchCode", captchCode);
            }

            HttpResponse response = HttpUtil.createPost(apiBaseUrl + "/login/checkLogin")
                    .header("Content-Type", "application/json")
                    .header("X-GEWE-TOKEN", token)
                    .body(params.toJSONString())
                    .execute();

            if (!response.isOk()) {
                return error(response.getStatus(), "请求失败");
            }

            JSONObject jsonResponse = JSON.parseObject(response.body());
            if (jsonResponse.getInteger("ret") != 200) {
                return error(jsonResponse.getInteger("ret"), jsonResponse.getString("msg"));
            }

            LoginStatusRespVO respVO = new LoginStatusRespVO();
            JSONObject data = jsonResponse.getJSONObject("data");
            respVO.setSuccess(data.getBoolean("success"));
            respVO.setWxId(data.getString("wxId"));
            respVO.setErrorMsg(data.getString("errorMsg"));
            
            // 如果登录成功，保存或更新账号信息
            if (respVO.getSuccess() && respVO.getWxId() != null) {
                WxAccountDO account = wxAccountMapper.selectByWxId(respVO.getWxId());
                if (account == null) {
                    account = new WxAccountDO();
                    account.setWxId(respVO.getWxId());
                    account.setAppId(appId);
                    account.setStatus(1); // 假设1为正常状态
                    wxAccountMapper.insert(account);
                } else if (!appId.equals(account.getAppId())) {
                    account.setAppId(appId);
                    wxAccountMapper.updateById(account);
                }
            }
            
            return success(respVO);
        } catch (Exception e) {
            return error(500, "系统异常：" + e.getMessage());
        }
    }

    @Override
    public CommonResult<String> getToken() {
        String token = getOrRefreshToken();
        return token != null ? success(token) : error(500, "获取token失败");
    }
    
    @Override
    public CommonResult<ContactListRespVO> getContactList(String wxId) {
        // 默认通过wxId查询appId
        WxAccountDO account = wxAccountMapper.selectByWxId(wxId);
        if (account == null) {
            return error(404, "未找到对应的微信账号");
        }
        return getContactList(wxId, account.getAppId(), true);
    }
    
    @Override
    public CommonResult<ContactListRespVO> getContactList(String wxId, String appId, boolean refresh) {
        // 如果不刷新，优先从数据库获取
        if (!refresh) {
            List<WxContactDO> contactList = wxContactMapper.selectListByWxIdAndAppId(wxId, appId);
            if (!contactList.isEmpty()) {
                ContactListRespVO respVO = new ContactListRespVO();
                List<ContactListRespVO.ContactItem> items = contactList.stream().map(contact -> {
                    ContactListRespVO.ContactItem item = new ContactListRespVO.ContactItem();
                    item.setWxId(contact.getFriendWxId());
                    item.setWxCode(contact.getWxCode());
                    item.setNickName(contact.getNickName());
                    item.setAvatar(contact.getAvatar());
                    item.setRemark(contact.getRemark());
                    return item;
                }).collect(Collectors.toList());
                respVO.setContacts(items);
                return success(respVO);
            }
        }
        
        // 从接口获取
        try {
            String token = getOrRefreshToken();
            if (token == null) {
                return error(500, "获取token失败");
            }

            JSONObject params = new JSONObject();
            params.put("wxId", wxId);
            params.put("appId", appId);

            HttpResponse response = HttpUtil.createPost(apiBaseUrl + "/contacts/getContactList")
                    .header("Content-Type", "application/json")
                    .header("X-GEWE-TOKEN", token)
                    .body(params.toJSONString())
                    .execute();

            if (!response.isOk()) {
                return error(response.getStatus(), "请求失败");
            }

            JSONObject jsonResponse = JSON.parseObject(response.body());
            if (jsonResponse.getInteger("ret") != 200) {
                return error(jsonResponse.getInteger("ret"), jsonResponse.getString("msg"));
            }

            ContactListRespVO respVO = new ContactListRespVO();
            List<ContactListRespVO.ContactItem> contactItems = new ArrayList<>();
            
            JSONObject data = jsonResponse.getJSONObject("data");
            if (data != null && data.containsKey("contacts")) {
                List<JSONObject> contacts = data.getJSONArray("contacts").toJavaList(JSONObject.class);
                for (JSONObject contact : contacts) {
                    ContactListRespVO.ContactItem item = new ContactListRespVO.ContactItem();
                    String friendWxId = contact.getString("wxId");
                    item.setWxId(friendWxId);
                    item.setWxCode(contact.getString("wxCode"));
                    item.setNickName(contact.getString("nickName"));
                    item.setAvatar(contact.getString("avatar"));
                    item.setRemark(contact.getString("remark"));
                    contactItems.add(item);
                    
                    // 保存到数据库
                    saveOrUpdateContact(wxId, friendWxId, appId, contact);
                }
            }
            
            respVO.setContacts(contactItems);
            return success(respVO);
        } catch (Exception e) {
            log.error("获取联系人列表异常", e);
            return error(500, "系统异常：" + e.getMessage());
        }
    }
    
    /**
     * 保存或更新联系人信息
     */
    private void saveOrUpdateContact(String wxId, String friendWxId, String appId, JSONObject contactData) {
        try {
            WxContactDO contact = wxContactMapper.selectByWxIdAndFriendWxIdAndAppId(wxId, friendWxId, appId);
            if (contact == null) {
                contact = new WxContactDO();
                contact.setWxId(wxId);
                contact.setFriendWxId(friendWxId);
                contact.setAppId(appId);
                contact.setWxCode(contactData.getString("wxCode"));
                contact.setNickName(contactData.getString("nickName"));
                contact.setAvatar(contactData.getString("avatar"));
                contact.setRemark(contactData.getString("remark"));
                wxContactMapper.insert(contact);
            } else {
                // 更新信息
                contact.setWxCode(contactData.getString("wxCode"));
                contact.setNickName(contactData.getString("nickName"));
                contact.setAvatar(contactData.getString("avatar"));
                contact.setRemark(contactData.getString("remark"));
                wxContactMapper.updateById(contact);
            }
        } catch (Exception e) {
            log.error("保存联系人信息异常", e);
        }
    }
    
    @Override
    public CommonResult<ContactDetailRespVO> getContactDetail(String wxId, String friendWxId) {
        // 默认通过wxId查询appId
        WxAccountDO account = wxAccountMapper.selectByWxId(wxId);
        if (account == null) {
            return error(404, "未找到对应的微信账号");
        }
        return getContactDetail(wxId, friendWxId, account.getAppId(), true);
    }
    
    @Override
    public CommonResult<ContactDetailRespVO> getContactDetail(String wxId, String friendWxId, String appId, boolean refresh) {
        // 如果不刷新，优先从数据库获取
        if (!refresh) {
            WxContactDO contact = wxContactMapper.selectByWxIdAndFriendWxIdAndAppId(wxId, friendWxId, appId);
            if (contact != null) {
                ContactDetailRespVO respVO = new ContactDetailRespVO();
                respVO.setWxId(contact.getFriendWxId());
                respVO.setWxCode(contact.getWxCode());
                respVO.setNickName(contact.getNickName());
                respVO.setAvatar(contact.getAvatar());
                respVO.setRemark(contact.getRemark());
                respVO.setPhoneNumber(contact.getPhoneNumber());
                respVO.setIsStarFriend(contact.getIsStarFriend());
                
                // 处理标签
                if (StringUtils.isNotBlank(contact.getTags())) {
                    respVO.setTags(contact.getTags().split(","));
                }
                
                return success(respVO);
            }
        }
        
        // 从接口获取
        try {
            String token = getOrRefreshToken();
            if (token == null) {
                return error(500, "获取token失败");
            }

            JSONObject params = new JSONObject();
            params.put("wxId", wxId);
            params.put("friendWxId", friendWxId);
            params.put("appId", appId);

            HttpResponse response = HttpUtil.createPost(apiBaseUrl + "/contacts/getContactDetail")
                    .header("Content-Type", "application/json")
                    .header("X-GEWE-TOKEN", token)
                    .body(params.toJSONString())
                    .execute();

            if (!response.isOk()) {
                return error(response.getStatus(), "请求失败");
            }

            JSONObject jsonResponse = JSON.parseObject(response.body());
            if (jsonResponse.getInteger("ret") != 200) {
                return error(jsonResponse.getInteger("ret"), jsonResponse.getString("msg"));
            }

            ContactDetailRespVO respVO = new ContactDetailRespVO();
            JSONObject data = jsonResponse.getJSONObject("data");
            if (data != null) {
                respVO.setWxId(data.getString("wxId"));
                respVO.setWxCode(data.getString("wxCode"));
                respVO.setNickName(data.getString("nickName"));
                respVO.setAvatar(data.getString("avatar"));
                respVO.setRemark(data.getString("remark"));
                respVO.setPhoneNumber(data.getString("phoneNumber"));
                respVO.setIsStarFriend(data.getBoolean("isStarFriend"));
                
                // 处理标签
                String[] tags = null;
                if (data.containsKey("tags") && data.getJSONArray("tags") != null) {
                    List<String> tagList = data.getJSONArray("tags").toJavaList(String.class);
                    tags = tagList.toArray(new String[0]);
                    respVO.setTags(tags);
                }
                
                // 保存到数据库
                saveOrUpdateContactDetail(wxId, friendWxId, appId, data, tags);
            }
            
            return success(respVO);
        } catch (Exception e) {
            log.error("获取联系人详情异常", e);
            return error(500, "系统异常：" + e.getMessage());
        }
    }
    
    /**
     * 保存或更新联系人详细信息
     */
    private void saveOrUpdateContactDetail(String wxId, String friendWxId, String appId, JSONObject data, String[] tags) {
        try {
            WxContactDO contact = wxContactMapper.selectByWxIdAndFriendWxIdAndAppId(wxId, friendWxId, appId);
            if (contact == null) {
                contact = new WxContactDO();
                contact.setWxId(wxId);
                contact.setFriendWxId(friendWxId);
                contact.setAppId(appId);
                contact.setWxCode(data.getString("wxCode"));
                contact.setNickName(data.getString("nickName"));
                contact.setAvatar(data.getString("avatar"));
                contact.setRemark(data.getString("remark"));
                contact.setPhoneNumber(data.getString("phoneNumber"));
                contact.setIsStarFriend(data.getBoolean("isStarFriend"));
                
                // 处理标签
                if (tags != null && tags.length > 0) {
                    contact.setTags(String.join(",", tags));
                }
                
                wxContactMapper.insert(contact);
            } else {
                // 更新信息
                contact.setWxCode(data.getString("wxCode"));
                contact.setNickName(data.getString("nickName"));
                contact.setAvatar(data.getString("avatar"));
                contact.setRemark(data.getString("remark"));
                contact.setPhoneNumber(data.getString("phoneNumber"));
                contact.setIsStarFriend(data.getBoolean("isStarFriend"));
                
                // 处理标签
                if (tags != null && tags.length > 0) {
                    contact.setTags(String.join(",", tags));
                }
                
                wxContactMapper.updateById(contact);
            }
        } catch (Exception e) {
            log.error("保存联系人详情异常", e);
        }
    }
    
    @Override
    public CommonResult<Boolean> updateContact(String wxId, ContactUpdateReqVO reqVO) {
        // 默认通过wxId查询appId
        WxAccountDO account = wxAccountMapper.selectByWxId(wxId);
        if (account == null) {
            return error(404, "未找到对应的微信账号");
        }
        return updateContact(wxId, reqVO, account.getAppId());
    }
    
    @Override
    @Transactional
    public CommonResult<Boolean> updateContact(String wxId, ContactUpdateReqVO reqVO, String appId) {
        try {
            String token = getOrRefreshToken();
            if (token == null) {
                return error(500, "获取token失败");
            }

            JSONObject params = new JSONObject();
            params.put("wxId", wxId);
            params.put("friendWxId", reqVO.getWxId());
            params.put("appId", appId);
            
            // 设置要更新的内容
            if (reqVO.getRemark() != null) {
                params.put("remark", reqVO.getRemark());
            }
            
            if (reqVO.getTags() != null) {
                params.put("tags", reqVO.getTags());
            }

            HttpResponse response = HttpUtil.createPost(apiBaseUrl + "/contacts/updateContact")
                    .header("Content-Type", "application/json")
                    .header("X-GEWE-TOKEN", token)
                    .body(params.toJSONString())
                    .execute();

            if (!response.isOk()) {
                return error(response.getStatus(), "请求失败");
            }

            JSONObject jsonResponse = JSON.parseObject(response.body());
            if (jsonResponse.getInteger("ret") != 200) {
                return error(jsonResponse.getInteger("ret"), jsonResponse.getString("msg"));
            }
            
            // 更新数据库
            WxContactDO contact = wxContactMapper.selectByWxIdAndFriendWxIdAndAppId(wxId, reqVO.getWxId(), appId);
            if (contact != null) {
                if (reqVO.getRemark() != null) {
                    contact.setRemark(reqVO.getRemark());
                }
                
                if (reqVO.getTags() != null) {
                    contact.setTags(String.join(",", reqVO.getTags()));
                }
                
                wxContactMapper.updateById(contact);
            }
            
            return success(true);
        } catch (Exception e) {
            log.error("更新联系人信息异常", e);
            return error(500, "系统异常：" + e.getMessage());
        }
    }
} 