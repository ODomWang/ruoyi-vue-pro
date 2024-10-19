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
    private long id;
    private String spiderUrl;
    private String spiderName;
    private String spiderModel;
    private long pingStatus;
    private long status;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;
    private long deptId;
    private String creator;
    private String updater;
    private String remark;
    private long spiderPageNum;
    private String bodyXpath;
    private String nextPageXpath;

}
