package cn.iocoder.yudao.module.wenxun.dal.dataobject.detailcheckinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 详情检测信息 DO
 *
 * @author 芋道源码
 */
@TableName("wenxun_detail_check_info")
@KeySequence("wenxun_detail_check_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCheckInfoDO extends BaseDO {

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

}