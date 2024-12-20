package cn.iocoder.yudao.module.wenxun.enums.commondao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WrongWordInfo {


    /**
     * 用于存储错误词汇的字符串变量
     * 主要用于记录在某些处理过程中发现的不正确或需要修正的词汇
     */
    private String wrongWord;

    /**
     * 用于存储正确词汇的字符串变量
     * 主要用于记录替代wrongWord或与之相对应的准确无误的词汇
     */
    private String rightWord;

    /**
     * 用于存储错误词汇类型的字符串变量
     * 主要用于描述wrongWord的错误性质或类别，比如拼写错误、语法错误等
     */
    private String wrongWordType;

    /**
     * 用于存储错误词汇类型的字符串变量
     * 主要用于描述wrongWord的错误性质或类别，比如拼写错误、语法错误等
     */
    private String wrongWordDesc;

    /**
     * 用于存储备注信息的字符串变量
     * 主要用于记录关于错误词汇、正确词汇或错误类型的一些额外说明或评论
     */
    private String remark;

    /**
     * 用于存储颜色类型的字符串变量 等级
     */
    private String colorType;
}
