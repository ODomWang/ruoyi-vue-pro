package cn.wenxun.spider;

import com.alibaba.fastjson.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeChatMessageUtil {

    // 微信公众号获取AccessToken接口
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    // 微信公众号消息发送接口
    private static final String MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
    private static final String MODEL_UTL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /**
     * 根据手机号推送消息
     *
     * @param phoneNumber 用户手机号
     * @param content     推送消息内容
     * @param appId       微信公众号AppID
     * @param appSecret   微信公众号AppSecret
     */
    public static void sendMessageToUser(String phoneNumber, String content, String appId, String appSecret) {
        try {
            // Step 1: 获取AccessToken
            String accessToken = getAccessToken(appId, appSecret);
            if (accessToken == null) {
                System.out.println("获取AccessToken失败！");
                return;
            }

            // Step 2: 获取用户OpenID
            String openId = getOpenIdByPhoneNumber(phoneNumber);
            if (openId == null) {
                System.out.println("获取用户OpenID失败！");
                return;
            }

            // Step 3: 推送消息
            String response = sendTextMessage(accessToken, openId, content);
            System.out.println("消息推送结果: " + response);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("推送消息失败: " + e.getMessage());
        }
    }

    /**
     * 获取微信公众号的AccessToken
     *
     * @param appId     微信公众号AppID
     * @param appSecret 微信公众号AppSecret
     * @return AccessToken
     */
    private static String getAccessToken(String appId, String appSecret) {
        try {
            String url = String.format(ACCESS_TOKEN_URL, appId, appSecret);
            String response = sendGetRequest(url);
            JSONObject jsonResponse = JSONObject.parseObject(response);
            System.out.println("获取AccessToken结果: " + jsonResponse);
            return jsonResponse.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 模拟通过手机号获取用户OpenID
     *
     * @param phoneNumber 用户手机号
     * @return 用户的OpenID
     */
    private static String getOpenIdByPhoneNumber(String phoneNumber) {
        // 模拟逻辑，实际场景需调用企业接口或查询数据库
        if ("13800000000".equals(phoneNumber)) {
            return "USER_OPEN_ID";
        }
        return null;
    }

    /**
     * 发送文本消息
     *
     * @param accessToken 微信接口调用凭据
     * @param openId      接收用户的OpenID
     * @param content     消息内容
     * @return 响应结果
     */
    private static String sendTextMessage(String accessToken, String openId, String content) {
        try {
            String requestUrl = MESSAGE_URL + accessToken;

            // 构建请求JSON
            JSONObject message = new JSONObject();
            message.put("touser", openId);
            message.put("msgtype", "text");

            JSONObject textContent = new JSONObject();
            textContent.put("content", content);
            message.put("text", textContent);

            // 发送请求
            return sendPostRequest(requestUrl, message.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 发送GET请求
     *
     * @param requestUrl 请求地址
     * @return 响应结果
     */
    private static String sendGetRequest(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return new String(conn.getInputStream().readAllBytes(), "UTF-8");
            } else {
                return "HTTP Error: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送POST请求
     *
     * @param requestUrl 请求地址
     * @param jsonParams JSON参数
     * @return 响应结果
     */
    private static String sendPostRequest(String requestUrl, String jsonParams) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // 发送数据
            OutputStream os = conn.getOutputStream();
            os.write(jsonParams.getBytes("UTF-8"));
            os.close();

            // 读取响应
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return new String(conn.getInputStream().readAllBytes(), "UTF-8");
            } else {
                return "HTTP Error: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private static String sendModelMessage(String accessToken, String openId,
                                           String s1, String s2, String s3, String s4, String s5, String s6) {
        try {
            String requestUrl = MODEL_UTL + accessToken;

            // 构建请求JSON
            JSONObject message = new JSONObject();
            message.put("touser", openId);
            message.put("template_id", "CS0Ju89xQJgRkuJAetwfI9i9PEcEWk8cWSZhNn5rSno");
            message.put("url", "http://wenxun.p8ai.com");
            message.put("topcolor", "#FF0000");

//网站信息:{{s1.DATA}} 文章标题:{{s2.DATA}} 错误类型:{{s3.DATA}} 错误等级:{{s4.DATA}} 问题信息:{{s5.DATA}} 检测时间:{{s6.DATA}}
            JSONObject textContent = new JSONObject();
            JSONObject value = new JSONObject();
            value.put("value", s1);
            value.put("color", "#1E2A47");
            textContent.put("thing1", value);
            JSONObject value2 = new JSONObject();
            value2.put("value", s2);
            value2.put("color", "#00FFFF");
            textContent.put("thing2", value2);
            JSONObject value3 = new JSONObject();
            value3.put("value", s3);
            value3.put("color", "#8A2BE2");
            textContent.put("thing3", value3);
            JSONObject value4 = new JSONObject();
            value4.put("value", s4);
            value4.put("color", "#FFD700");
            textContent.put("thing4", value4);
            JSONObject value5 = new JSONObject();
            value5.put("value", s5);
            value5.put("color", "#32CD32");
            textContent.put("thing5", value5);
            JSONObject value6 = new JSONObject();
            value6.put("value", s6);
            value6.put("color", "#C0C0C0");
            textContent.put("time", value6);
            message.put("data", textContent);

            // 发送请求
            return sendPostRequest(requestUrl, message.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        // 示例：推送消息
        String phoneNumber = "18888043549"; // 用户手机号
        String messageContent = "你好，这是测试推送消息！";
        String appId = "wxf961dfb4d73e841c"; // 替换为真实的AppID
        String appSecret = "6e72a0e991cebff4d2599b64b13a3c3e"; // 替换为真实的AppSecret
        String modelId = "_N5060kFXBvR6a8gme4wTEZ30xnFgfss79ElIyhIorA";
//网站信息:{{s1.DATA}} 文章标题:{{s2.DATA}} 错误类型:{{s3.DATA}} 错误等级:{{s4.DATA}} 问题信息:{{s5.DATA}} 检测时间:{{s6.DATA}}

        sendModelMessage(getAccessToken(appId,appSecret), "oku5H7E5HUgecdRexDDcmyrD-d7E", "苏州科技大学-政务信息",
                "我校当选人才联合培养工作委员会主任单位", "错别字", "警告", "中华民国", "2025-01-08 14:50:32");
    }
}
