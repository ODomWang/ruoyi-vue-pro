package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 联系人详情 Response VO")
@Data
public class ContactDetailRespVO {

    @Schema(description = "微信ID", required = true, example = "wxid_123456")
    private String wxId;

    @Schema(description = "微信号", example = "wx_123456")
    private String wxCode;

    @Schema(description = "昵称", example = "张三")
    private String nickName;

    @Schema(description = "头像", example = "http://xxx.com/avatar.jpg")
    private String avatar;

    @Schema(description = "备注", example = "同事")
    private String remark;
    
    @Schema(description = "标签列表", example = "['朋友', '同事']")
    private String[] tags;
    
    @Schema(description = "手机号", example = "13800138000")
    private String phoneNumber;
    
    @Schema(description = "是否星标好友", example = "true")
    private Boolean isStarFriend;
} 