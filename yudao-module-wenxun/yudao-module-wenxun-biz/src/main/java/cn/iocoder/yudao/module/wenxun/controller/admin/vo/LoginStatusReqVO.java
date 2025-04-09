package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import lombok.Data;

@Data
public class LoginStatusReqVO {
    private String appId;
    private String captchaCode;
    private String uuid;

}