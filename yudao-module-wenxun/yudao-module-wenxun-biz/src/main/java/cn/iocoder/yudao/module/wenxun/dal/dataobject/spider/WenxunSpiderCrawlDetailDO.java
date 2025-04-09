package cn.iocoder.yudao.module.wenxun.dal.dataobject.spider;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 文巡爬虫采集数据 DO
 */
@TableName("wenxun_spider_crawl_detail")
@KeySequence("wenxun_spider_crawl_detail_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WenxunSpiderCrawlDetailDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 爬虫URL
     */
    private String spiderUrl;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 日期
     */
    private String date;

    /**
     * 作者
     */
    private String author;

    /**
     * 备注
     */
    private String remark;

    /**
     * 采集配置编号
     */
    private Long spiderConfigId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 标题描述
     */
    private String titleDesc;

    /**
     * 网站图标
     */
    private String icon;

    /**
     * 部门编号
     */
    private Long deptId;

    /**
     * 是否删除
     */
    private Boolean deleted;
} 