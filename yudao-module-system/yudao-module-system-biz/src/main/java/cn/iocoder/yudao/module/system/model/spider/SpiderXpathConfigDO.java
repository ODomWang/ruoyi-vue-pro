package cn.iocoder.yudao.module.system.model.spider;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpiderXpathConfigDO {

    private Long id;
    private String spiderUrl;
    private String spiderName;
    private String spiderModel;
    private String remark;
    private Long spiderPageNum;
    //xpath 配置
    private String bodyXpath;
    private String nextPageXpath;
    private String listXpath;
    private String titleXpath;
    private String dateXpath;
    private String descXpath;
    private String itemXpath;
    @NotNull(message = "部门名称不能为空")
    private Long deptId;


    public String getListXpath() {
        return listXpath;
    }

    public String getTitleXpath() {
        if (listXpath != null) {
            return titleXpath = titleXpath.replace(listXpath + getItemXpathV2(), "");
        }
        return titleXpath;
    }

    public String getDescXpath() {
        if (listXpath != null) {
            return descXpath = descXpath.replace(listXpath + getItemXpathV2(), "");
        }
        return descXpath;
    }

    public String getItemXpathV2() {
        if (listXpath != null) {
            return itemXpath.replace(listXpath, "");
        }
        return "";
    }

    public String getItemXpath() {
        if (listXpath != null) {
            return itemXpath.replace(listXpath, "").replaceAll("\\[\\d+]$", "[*]");
        }
        return itemXpath;
    }
}
