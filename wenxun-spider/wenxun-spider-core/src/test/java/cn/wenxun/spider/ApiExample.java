package cn.wenxun.spider;

import cn.iocoder.yudao.HttpUtil;
import cn.iocoder.yudao.support.HttpHeader;
import cn.iocoder.yudao.support.SimpleHttpResponse;
import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ApiExample {
    public static String url = "http://110.40.228.6:2531/v2/api";

    //https://apifox.com/apidoc/shared-69ba62ca-cb7d-437e-85e4-6f3d3df271b1/api-196794512
    public static void main(String[] args) throws InterruptedException {
//        test2();
//        test();
//        test3();
//        test4();
//        test5();
//        test();
        List<Map<String, Object>> converted = convertExcelToJson("C:\\Users\\wangjp\\Documents\\WeChat Files\\wxid_1vz8x53gemr521\\FileStorage\\File\\2025-03\\柳职大食堂信息.xlsx");
        int i = 0;
        for (Map<String, Object> map : converted) {
            String content = buildAppMsgXml(map);
            test4(content);
            Thread.sleep(1000);
            i++;
            if (i == 10) {
                return;
            }
        }
    }

    public static void test() {

        String appuid = "wx_jez2zs-BjU9lDUfbvFy8B";
        String uri = "/message/postText";
        Map<String, Object> headers = new HashMap<>();

        headers.put("appId", appuid);
        headers.put("toWxid", "2912609774@chatroom");
        headers.put("ats", "wxid_1vz8x53gemr521,wxid_5377753776112");
        headers.put("content", "摘要：也是服了\n" +
                "这辈子遇到的最破烂的支付系统\n" +
                "想交钱但交不上可还行\n" +
                "复试80元竟然被限额\n" +
                "#study25# \u200B\u200B\n" +
                "链接：http://weibo.com/7862432556/PjkkhkWeY\n" +
                "时间：2025-03-20 00:10\n" +
                "来源：新浪微博\n" +
                "作者：永远Uppp_\n" +
                "属性：负面\n" +
                "涉及关键词：中国矿业大学\n" +
                "转发数：0\n" +
                "评论数：0");


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
        headers.put("title", "这辈子遇到的最破烂的支付系统");
        headers.put("toWxid", "2912609774@chatroom");
//        headers.put("ats", "wxid_1vz8x53gemr521,wxid_5377753776112");
        headers.put("desc", "也是服了这辈子遇到的最破烂的支付系统想交钱但交不上可还行复试80元竟然被限额");
        headers.put("linkUrl", "http://weibo.com/7862432556/PjkkhkWeY");

        headers.put("thumbUrl", "https://pics3.baidu.com/feed/0824ab18972bd407a9403f336648d15c0db30943.jpeg@f_auto?token=d26f7f142871542956aaa13799ba1946");

        HttpHeader headers2 = new HttpHeader();

        headers2.add("X-GEWE-TOKEN", "ff09c0cf46cb49bab25d420c47196934");
        headers2.add("Content-Type", "application/json");
        SimpleHttpResponse response = HttpUtil.post(url + uri, JSON.toJSONString(headers), headers2);
        System.out.println(response.getBody());

    }

    public static void test4(String content) {
        String uri = "/message/postAppMsg";
        String appuid = "wx_jez2zs-BjU9lDUfbvFy8B";
        Map<String, Object> headers = new HashMap<>();

        headers.put("appId", appuid);
        headers.put("toWxid", "2912609774@chatroom");
        headers.put("appmsg", content);

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


    /**
     * 读取 Excel（.xlsx）文件并转换为 JSON 字符串
     *
     * @param filePath Excel 文件路径
     * @return JSON 字符串
     */
    public static List<Map<String, Object>> convertExcelToJson(String filePath) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            List<String> headers = new ArrayList<>();

            // 读取表头
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                for (Cell cell : headerRow) {
                    headers.add(cell.getStringCellValue());
                }
            }

            // 读取数据行
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, Object> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowMap.put(headers.get(i), getCellValue(cell));
                }
                resultList.add(rowMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList; // pretty print
    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return null;
        }
    }

    public static String buildAppMsgXml(Map<String, Object> data) {
        String title = getString(data, "标题/微博内容");
        String des = getString(data, "摘要");
        String url = getString(data, "原文/评论链接");
        String source = getString(data, "来源网站");
        String author = getString(data, "原文作者");

        // 模拟缩略图（如有实际图 URL，可换掉）
        String thumbUrl = "https://mmbiz.qpic.cn/sz_mmbiz_jpg/xrFYciaHL08DCJtwQefqrH8JcohbOHhTpyCPab8IgDibkTv3Pspicjw8TRHnoic2tmiafBtUHg7ObZznpWocwkCib6Tw/640";

        return "<appmsg appid=\"\" sdkver=\"0\">\n" +
                "    <title>" + escapeXml(title) + "</title>\n" +
                "    <des>" + escapeXml(des) + "</des>\n" +
                "    <action />\n" +
                "    <type>5</type>\n" +
                "    <showtype>0</showtype>\n" +
                "    <soundtype>0</soundtype>\n" +
                "    <mediatagname />\n" +
                "    <messageext />\n" +
                "    <messageaction />\n" +
                "    <content />\n" +
                "    <contentattr>0</contentattr>\n" +
                "    <url>" + escapeXml(url) + "</url>\n" +
                "    <lowurl />\n" +
                "    <dataurl />\n" +
                "    <lowdataurl />\n" +
                "    <appattach>\n" +
                "        <totallen>0</totallen>\n" +
                "        <attachid />\n" +
                "        <emoticonmd5 />\n" +
                "        <fileext />\n" +
                "        <cdnthumburl>" + escapeXml(thumbUrl) + "</cdnthumburl>\n" +
                "        <cdnthumbmd5 />\n" +
                "        <cdnthumblength>0</cdnthumblength>\n" +
                "        <cdnthumbwidth>0</cdnthumbwidth>\n" +
                "        <cdnthumbheight>0</cdnthumbheight>\n" +
                "        <cdnthumbaeskey />\n" +
                "        <aeskey />\n" +
                "        <encryver>0</encryver>\n" +
                "    </appattach>\n" +
                "    <extinfo />\n" +
                "    <sourceusername>" + escapeXml(source + "_" + author) + "</sourceusername>\n" +
                "    <sourcedisplayname>" + escapeXml(source + " · " + author) + "</sourcedisplayname>\n" +
                "    <thumburl>" + escapeXml(thumbUrl) + "</thumburl>\n" +
                "    <md5 />\n" +
                "    <statextstr />\n" +
                "    <mmreadershare>\n" +
                "        <itemshowtype>0</itemshowtype>\n" +
                "    </mmreadershare>\n" +
                "</appmsg>";
    }

    private static String getString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val != null ? val.toString() : "";
    }

    private static String escapeXml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }


}