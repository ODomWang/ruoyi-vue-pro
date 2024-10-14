package cn.wenxun.admin.controller.model;

import lombok.Data;

@Data
public class SchoolWebInfo {
    /**
     * 模块名称
     */
    private String modelName;
    /**
     * 模块url
     */
    private String modelUrl;
    /**
     * 模块描述
     */
    private String modelDesc;
    /**
     * 模块图标
     */
    private String modelIcon;

    /**
     * id
     */
    private String id;

    /**
     * 上级模块id
     */
    private String parentId;

    /**
     * 所属网站地址
     */
    private String webUrl;

    /**
     * 所属网站名称
     */
    private String webName;

    /**
     * 网站归属人
     */
    private String webOwner;
}
