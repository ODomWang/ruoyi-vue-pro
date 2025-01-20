package cn.wenxun.admin.model;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeiliSearchInfo extends PageParam {


    public String spiderId;

    public String keyWord;

    public String status;

    // 时间范围
    private LocalDateTime[] createTime;

}
