package cn.wenxun.spider;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class WeComMessageUtils {

    // 企业微信自定义应用接口配置
    private static final String CORP_ID = "ww224258be858951bd";
    private static final String SECRET = "Kh5NPQcFaXLlCqvnBsJbU1zojphpDqmpREoYpOJcpHQ";
    private static final String AGENT_ID = "1000002";

    // 企业微信群机器人配置
    private static final String WEBHOOK_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=YOUR_WEBHOOK_KEY";

    private static final OkHttpClient CLIENT = new OkHttpClient();

    /**
     * 获取 Access Token
     *
     * @return Access Token
     * @throws IOException 获取失败时抛出异常
     */
    private static String getAccessToken() throws IOException {
        String url = String.format(
                "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s",
                CORP_ID, SECRET);

        Request request = new Request.Builder().url(url).get().build();
        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
                return jsonObject.get("access_token").getAsString();
            } else {
                throw new IOException("Failed to get access token: " + response.message());
            }
        }
    }

    /**
     * 推送消息到指定用户（自定义应用接口）
     *
     * @param toUser  接收用户 ID，多个用户用 '|' 分隔，@all 发送给所有人
     * @param content 消息内容
     * @throws IOException 发送失败时抛出异常
     */
    public static void sendMessageToUser(String toUser, String content) throws IOException {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            throw new IllegalStateException("Failed to obtain access token");
        }

        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

        JsonObject message = new JsonObject();
        message.addProperty("touser", toUser);
        message.addProperty("msgtype", "text");
        message.addProperty("agentid", AGENT_ID);
        JsonObject text = new JsonObject();
        text.addProperty("content", content);
        message.add("text", text);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), message.toString());
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("User Message Sent: " + response.body().string());
            } else {
                throw new IOException("Failed to send message: " + response.message());
            }
        }
    }

    /**
     * 推送消息到群组（群机器人接口）
     *
     * @param content 消息内容
     * @throws IOException 发送失败时抛出异常
     */
    public static void sendMessageToGroup(String content) throws IOException {
        JsonObject message = new JsonObject();
        message.addProperty("msgtype", "text");
        JsonObject text = new JsonObject();
        text.addProperty("content", content);
        message.add("text", text);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), message.toString());
        Request request = new Request.Builder().url(WEBHOOK_URL).post(body).build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Group Message Sent: " + response.body().string());
            } else {
                throw new IOException("Failed to send group message: " + response.message());
            }
        }
    }

    /**
     * 发送富文本消息（高端精致版）
     *
     * @param userId 接收用户的 UserID
     */
    public static void sendElegantMpnewsMessage(String userId) throws IOException {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            throw new IllegalStateException("Failed to obtain access token");
        }
        String apiUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

        // 构造 JSON 请求体
        String payload = String.format("""
                        {
                             "touser" : "%s",
                              "msgtype": "markdown",
                             "agentid" : "%s",
                             "markdown": {
                                  "content": "**通知：敏感信息审核通知**
                                                                                           
                                                                                           尊敬的管理员：
                                                                                           
                                                                                           我们检测到**文巡智检**出现疑似敏感信息。请尽快查看并审核以下内容：
                                                                                           
                                                                                           **检测时间：**  2025-01-15 14:30:00
                                                                                           
                                                                                           **涉及页面：** [点击查看页面](http://110.40.228.6)
                                                                                           
                                                                                           **审核内容：**  包含敏感词 **“违规词”**，内容可能涉及 **法律风险**。
                                                                                           
                                                                                           **操作：** 点击以下链接完成审核：\n\n  [立即审核](http://110.40.228.6/mall/fontCheck/sensitive/index)
                                                                                           
                                                                                           如需帮助，请联系技术支持：  400-123-4567
                                                                                           
                                                      "
                             },
                             "enable_duplicate_check": 0,
                             "duplicate_check_interval": 1800
                          }
                """, userId, AGENT_ID);

        // 发起 HTTP 请求
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(payload, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(apiUrl).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }

    public static String getUserIdByPhone(String number) throws IOException {

        String accessToken = getAccessToken();
        if (accessToken == null) {
            throw new IllegalStateException("Failed to obtain access token");
        }

        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserid?access_token=" + accessToken;

        JsonObject message = new JsonObject();
        message.addProperty("mobile", number);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), message.toString());
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                response.close();
                return responseBody;
            } else {
                throw new IOException("Failed to send group message: " + response.message());
            }
        }
    }

    public static void main(String[] args) {

        try {
            System.out.println(getUserIdByPhone("18888043549"));
//            String userId = "YuanTangJun|WangJingPeng";
//            sendElegantMpnewsMessage(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
