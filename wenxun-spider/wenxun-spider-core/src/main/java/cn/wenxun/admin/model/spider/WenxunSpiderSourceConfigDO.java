package cn.wenxun.admin.model.spider;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Getter
@TableName("wenxun_spider_source_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WenxunSpiderSourceConfigDO {

    @TableId
    private Long id;
    private String spiderUrl;
    private String spiderName;
    private String spiderModel;
    private Long pingStatus;
    private Long status;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;
    private Long deptId;
    private String creator;
    private String updater;
    private String remark;
    private Long spiderPageNum;
    private String bodyXpath;
    private String nextPageXpath;
    private String listXpath;
    private String titleXpath;
    private String dateXpath;
    private String descXpath;
    private String itemXpath;

}
