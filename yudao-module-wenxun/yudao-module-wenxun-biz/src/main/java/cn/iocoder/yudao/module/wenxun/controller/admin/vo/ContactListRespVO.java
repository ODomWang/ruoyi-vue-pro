package cn.iocoder.yudao.module.wenxun.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 联系人列表 Response VO")
@Data
public class ContactListRespVO {

    @Schema(description = "联系人列表", required = true)
    private List<ContactItem> contacts;

    @Data
    public static class ContactItem {
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
    }
} 