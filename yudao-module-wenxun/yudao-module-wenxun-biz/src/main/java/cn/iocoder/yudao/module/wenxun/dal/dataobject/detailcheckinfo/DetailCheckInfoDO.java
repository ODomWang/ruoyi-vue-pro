package cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckinfo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 详情检测信息 DO
 *
 * @author 芋道源码
 */
@TableName("wenxun_detail_check_info")
@KeySequence("wenxun_detail_check_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCheckInfoDO {
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 检查源，1 敏感词，2，错词，3，接口
     */
    private Integer checkSource;
    /**
     * 错词详情
     */
    private String checkDetail;
    /**
     * 修正词汇
     */
    private String targetDetail;
    /**
     * 数据状态
     */
    private Integer status;


    /**
     * 源地址
     */
    private String sourceUrl;

    /**
     * 源地址
     */
    private String spiderConfigId;
    /**
     * 源地址
     */
    private String webIcon;
    /**
     * 源地址
     */
    private String titleDesc;



}