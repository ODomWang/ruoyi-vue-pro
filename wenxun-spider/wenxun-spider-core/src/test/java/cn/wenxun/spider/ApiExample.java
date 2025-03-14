package cn.wenxun.spider;

import cn.iocoder.yudao.HttpUtil;
import cn.iocoder.yudao.support.HttpHeader;
import cn.iocoder.yudao.support.SimpleHttpResponse;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class ApiExample {
    public static String url = "http://110.40.228.6:2531/v2/api";
//https://apifox.com/apidoc/shared-69ba62ca-cb7d-437e-85e4-6f3d3df271b1/api-196794512
    public static void main(String[] args) {
//        test2();
//        test();
//        test3();
//        test4();
        test5();
    }

    public static void test() {

        String appuid = "wx_jez2zs-BjU9lDUfbvFy8B";
        String uri = "/message/postText";
        Map<String, Object> headers = new HashMap<>();

        headers.put("appId", appuid);
        headers.put("toWxid", "2912609774@chatroom");
        headers.put("ats", "wxid_1vz8x53gemr521,wxid_5377753776112");
        headers.put("content", "【待审核通知】\n  你的关注网站【翠花大学校园新闻网】 \n  命中敏感词信息 \n  请尽快处理 @D @袁堂俊");


        HttpHeader headers2 = new HttpHeader();

        headers2.add("X-GEWE-TOKEN", "ff09c0cf46cb49bab25d420c47196934");
        headers2.add("Content-Type", "application/json");
        SimpleHttpResponse response = HttpUtil.post(url + uri, JSON.toJSONString(headers), headers2);
        System.out.println(response.getBody());
    }
    public static void test2() {
        String uri = "/contacts/fetchContactsList";
        String appuid = "wx_jez2zs-BjU9lDUfbvFy8B";
         Map<String, Object> headers = new HashMap<>();

        headers.put("appId", appuid);

        HttpHeader headers2 = new HttpHeader();

        headers2.add("X-GEWE-TOKEN", "ff09c0cf46cb49bab25d420c47196934");
        headers2.add("Content-Type", "application/json");
        SimpleHttpResponse response = HttpUtil.post(url + uri, JSON.toJSONString(headers), headers2);
        System.out.println(response.getBody());
    }

    public static void test3() {
        String uri = "/message/postLink";
        String appuid = "wx_jez2zs-BjU9lDUfbvFy8B";
        Map<String, Object> headers = new HashMap<>();

        headers.put("appId", appuid);
        headers.put("title", "待审核通知");
        headers.put("toWxid", "2912609774@chatroom");
//        headers.put("ats", "wxid_1vz8x53gemr521,wxid_5377753776112");
        headers.put("desc", "网站【翠花大学校园新闻网】命中敏感词信息");
        headers.put("linkUrl", "http://wenxun.p8ai.com/login?redirect=/dashboard");

        headers.put("thumbUrl", "https://pics3.baidu.com/feed/0824ab18972bd407a9403f336648d15c0db30943.jpeg@f_auto?token=d26f7f142871542956aaa13799ba1946");

        HttpHeader headers2 = new HttpHeader();

        headers2.add("X-GEWE-TOKEN", "ff09c0cf46cb49bab25d420c47196934");
        headers2.add("Content-Type", "application/json");
        SimpleHttpResponse response = HttpUtil.post(url + uri, JSON.toJSONString(headers), headers2);
        System.out.println(response.getBody());

    }
    public static void test4() {
        String uri = "/message/postAppMsg";
        String appuid = "wx_jez2zs-BjU9lDUfbvFy8B";
        Map<String, Object> headers = new HashMap<>();

        headers.put("appId", appuid);
         headers.put("toWxid", "2912609774@chatroom");
         headers.put("appmsg", "<appmsg appid=\"\" sdkver=\"0\">\n" +
                 "\t\t<title>网站内容审核通知，请处理</title>\n" +
                 "\t\t<des />\n" +
                 "\t\t<action />\n" +
                 "\t\t<type>5</type>\n" +
                 "\t\t<showtype>0</showtype>\n" +
                 "\t\t<soundtype>0</soundtype>\n" +
                 "\t\t<mediatagname />\n" +
                 "\t\t<messageext />\n" +
                 "\t\t<messageaction />\n" +
                 "\t\t<content />\n" +
                 "\t\t<contentattr>0</contentattr>\n" +
                 "\t\t<url>http://wenxun.p8ai.com</url>\n" +
                 "\t\t<lowurl />\n" +
                 "\t\t<dataurl />\n" +
                 "\t\t<lowdataurl />\n" +
                 "\t\t<appattach>\n" +
                 "\t\t\t<totallen>0</totallen>\n" +
                 "\t\t\t<attachid />\n" +
                 "\t\t\t<emoticonmd5 />\n" +
                 "\t\t\t<fileext />\n" +
                 "\t\t\t<cdnthumburl>3057020100044b304902010002048399cc8402032f57ed02041388e6720204658e922d042462666538346165322d303035382d343262322d616538322d3337306231346630323534360204051408030201000405004c53d900</cdnthumburl>\n" +
                 "\t\t\t<cdnthumbmd5>ea3d5e8d4059cb4db0a3c39c789f2d6f</cdnthumbmd5>\n" +
                 "\t\t\t<cdnthumblength>93065</cdnthumblength>\n" +
                 "\t\t\t<cdnthumbwidth>1080</cdnthumbwidth>\n" +
                 "\t\t\t<cdnthumbheight>459</cdnthumbheight>\n" +
                 "\t\t\t<cdnthumbaeskey>849df42ab37c8cadb324fe94ba46d76e</cdnthumbaeskey>\n" +
                 "\t\t\t<aeskey>849df42ab37c8cadb324fe94ba46d76e</aeskey>\n" +
                 "\t\t\t<encryver>0</encryver>\n" +
                 "\t\t</appattach>\n" +
                 "\t\t<extinfo />\n" +
                 "\t\t<sourceusername>gh_363b924965e9</sourceusername>\n" +
                 "\t\t<sourcedisplayname>文巡智检</sourcedisplayname>\n" +
                 "\t\t<thumburl>https://pics3.baidu.com/feed/0824ab18972bd407a9403f336648d15c0db30943.jpeg@f_auto?token=d26f7f142871542956aaa13799ba1946</thumburl>\n" +
                 "\t\t<md5 />\n" +
                 "\t\t<statextstr />\n" +
                 "\t\t<mmreadershare>\n" +
                 "\t\t\t<itemshowtype>0</itemshowtype>\n" +
                 "\t\t</mmreadershare>\n" +
                 "\t</appmsg>\n");

        HttpHeader headers2 = new HttpHeader();

        headers2.add("X-GEWE-TOKEN", "ff09c0cf46cb49bab25d420c47196934");
        headers2.add("Content-Type", "application/json");
        SimpleHttpResponse response = HttpUtil.post(url + uri, JSON.toJSONString(headers), headers2);
        System.out.println(response.getBody());

    }

    public static void test5() {
        String uri = "/contacts/search";
        String appuid = "wx_jez2zs-BjU9lDUfbvFy8B";
        Map<String, Object> headers = new HashMap<>();

        headers.put("appId", appuid);
        headers.put("contactsInfo", "13655142767");

        HttpHeader headers2 = new HttpHeader();

        headers2.add("X-GEWE-TOKEN", "ff09c0cf46cb49bab25d420c47196934");
        headers2.add("Content-Type", "application/json");
        SimpleHttpResponse response = HttpUtil.post(url + uri, JSON.toJSONString(headers), headers2);
        System.out.println(response.getBody());
     }

}