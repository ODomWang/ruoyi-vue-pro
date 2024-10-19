package cn.wenxun.admin.model.spider;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Getter
@TableName("wenxun_spider_crawl_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WenxunSpiderCrawlDetail {

    @TableId
    private long id;
    private String spiderUrl;
    private String title;
    private String content;
    private String date;
    private String author;
    private String remark;
    private String titleDesc;
    private long spiderConfigId;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;
    private long status;



}
