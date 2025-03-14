package cn.iocoder.yudao.module.system.model.spider;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.data.annotation.Transient;

@Getter
@TableName("wenxun_spider_crawl_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WenxunSpiderCrawlDetail {

    @TableId
    private Long id;
    private String spiderUrl;
    private String title;
    private String content;
    private String date;
    private String author;
    private String remark;
    private String titleDesc;
    private Long spiderConfigId;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;
    private Long status;
    private String icon;
    @TableField(exist = false)
    private String spiderName;
    private Long deptId;




}
