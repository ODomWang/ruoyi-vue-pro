package cn.wenxun.admin.controller.model;

import lombok.Data;

@Data
public class NewsInfo {
    private String title;
    private String content;
    private String url;
    private String date;
    private String author;
    private String desc;
}
