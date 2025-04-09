package cn.iocoder.yudao.module.wenxun.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wenxun.controller.admin.vo.*;
import cn.iocoder.yudao.module.wenxun.convert.auth.WenxunUserConvert;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.WenxunUserDO;
import cn.iocoder.yudao.module.wenxun.service.api.WenxunApiService;
import cn.iocoder.yudao.module.wenxun.service.wenxun.WenxunUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 文巡认证")
@RestController
@RequestMapping("/wenxun/auth")
@Validated
@Slf4j
public class WenxunAuthController {

    @Resource
    private WenxunApiService wenxunApiService;

    @Resource
    private WenxunUserService wenxunUserService;

    @PostMapping("/login")
    @Operation(summary = "统一登录接口")
    public CommonResult<WenxunLoginRespVO> login() {
        return wenxunUserService.login();
    }

    @PostMapping("/getToken")
    @Operation(summary = "获取Token")
    public CommonResult<String> getToken() {
        return wenxunApiService.getToken();
    }

    @PostMapping("/getLoginQrCode")
    @Operation(summary = "获取登录二维码")
    @Parameter(name = "appId", description = "应用ID，新设备登录时必传")
    public CommonResult<LoginQrCodeRespVO> getLoginQrCode(@RequestBody LoginStatusReqVO appId) {
        return wenxunApiService.getLoginQrCode(appId.getAppId());
    }

    @PostMapping("/checkLoginStatus")
    @Operation(summary = "检查登录状态")
    @Parameters({
            @Parameter(name = "appId", description = "应用ID", required = true),
            @Parameter(name = "captchCode", description = "验证码(可选,扫码后手机提示输入时需要)")
    })
    public CommonResult<LoginStatusRespVO> checkLoginStatus(@RequestBody LoginStatusReqVO reqVO) {
        return wenxunApiService.checkLoginStatus(reqVO.getAppId(), reqVO.getCaptchaCode(), reqVO.getUuid());
    }


    @GetMapping("/getUser")
    @Operation(summary = "获取用户信息")
    @Parameter(name = "appId", description = "应用ID", required = true)
    public CommonResult<WenxunUserRespVO> getUser(@RequestBody LoginStatusReqVO reqVO) {
        WenxunUserDO user = wenxunUserService.getUserByAppId(reqVO.getAppId());
        return success(WenxunUserConvert.INSTANCE.convert(user));
    }

} 