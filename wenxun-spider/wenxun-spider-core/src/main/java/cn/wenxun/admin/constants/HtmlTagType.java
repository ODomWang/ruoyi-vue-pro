package cn.wenxun.admin.constants;

public enum HtmlTagType {
    A("a"),          // 超链接
    DIV("div"),      // 容器标签
    SPAN("span"),    // 内联容器
    P("p"),          // 段落
    H1("h1"),        // 标题1
    H2("h2"),        // 标题2
    H3("h3"),        // 标题3
    H4("h4"),        // 标题4
    H5("h5"),        // 标题5
    H6("h6"),        // 标题6
    IMG("img"),      // 图像
    INPUT("input"),  // 输入框
    BUTTON("button"),// 按钮
    TABLE("table"),  // 表格
    TR("tr"),        // 表格行
    TD("td"),        // 表格单元格
    TH("th"),        // 表格标题单元格
    UL("ul"),        // 无序列表
    OL("ol"),        // 有序列表
    LI("li"),        // 列表项
    FORM("form"),    // 表单
    LABEL("label"),  // 标签
    SELECT("select"),// 下拉列表
    OPTION("option"),// 选项
    TEXTAREA("textarea"); // 文本区域

    private final String tag;

    HtmlTagType(String tag) {
        this.tag = tag;
    }

    public String getXpathTag() {
        return ".//"+tag;
    }

    @Override
    public String toString() {
        return tag;
    }
}
