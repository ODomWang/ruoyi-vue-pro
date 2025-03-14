package cn.iocoder.yudao.module.system.model;

import lombok.Data;

import java.util.List;

@Data
public class NewsInfo {
    private String title;
    private String content;
    private String url;
    private String date;
    private String author;
    private String desc;
    private String nextPageUrl;
    private String imgUrl;
    private List<String> ContentImgUrlList;
    private String webIcon;
    private String spiderName;
    private long deptId;

    private long configId;

}
