package cn.iocoder.yudao.module.wenxun.service.wenxun.impl;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.vo.WenxunLoginRespVO;
import cn.iocoder.yudao.module.wenxun.controller.admin.vo.LoginQrCodeRespVO;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WenxunUserDO;
import cn.iocoder.yudao.module.wenxun.dal.mysql.WenxunUserMapper;
import cn.iocoder.yudao.module.wenxun.service.api.WenxunApiService;
import cn.iocoder.yudao.module.wenxun.service.wenxun.WenxunUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wenxun.enums.ErrorCodeConstants.USER_NOT_EXISTS;

/**
 * 文巡用户 Service 实现类
 */
@Service
@Validated
public class WenxunUserServiceImpl implements WenxunUserService {

    @Resource
    private WenxunApiService wenxunApiService;

    @Resource
    private WenxunUserMapper wenxunUserMapper;

    @Override
    public Long createUser(WenxunUserDO user) {
        wenxunUserMapper.insert(user);
        return user.getId();
    }

    @Override
    public void updateUser(WenxunUserDO user) {
        // 校验存在
        validateUserExists(user.getId());
        // 更新
        wenxunUserMapper.updateById(user);
    }

    @Override
    public WenxunUserDO getUser(Long id) {
        return wenxunUserMapper.selectById(id);
    }

    @Override
    public WenxunUserDO getUserByAppId(String appId) {
        return wenxunUserMapper.selectOne(WenxunUserDO::getAppId, appId);
    }

    @Override
    public CommonResult<WenxunLoginRespVO> login() {
        // 1. 获取token
        CommonResult<String> tokenResult = wenxunApiService.getToken();
        if (!tokenResult.isSuccess()) {
            return error(tokenResult.getCode(), tokenResult.getMsg());
        }

        // 2. 生成appId
        String appId = "";

        // 3. 获取登录二维码
        CommonResult<LoginQrCodeRespVO> qrCodeResult = wenxunApiService.getLoginQrCode(appId);
        if (!qrCodeResult.isSuccess()) {
            return error(qrCodeResult.getCode(), qrCodeResult.getMsg());
        }

        // 4. 组装返回结果
        WenxunLoginRespVO loginResp = new WenxunLoginRespVO();
        loginResp.setAppId(qrCodeResult.getData().getAppId());
        loginResp.setQrImgBase64(qrCodeResult.getData().getQrImgBase64());
        loginResp.setQrData(qrCodeResult.getData().getQrData());
        loginResp.setUuid(qrCodeResult.getData().getUuid());

        loginResp.setStatus(0); // 初始状态为未登录

        return success(loginResp);
    }

    private void validateUserExists(Long id) {
        if (wenxunUserMapper.selectById(id) == null) {
            throw exception(USER_NOT_EXISTS);
        }
    }

} 