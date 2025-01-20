package cn.wenxun.admin.controller.admin.message;

import cn.hutool.core.util.XmlUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

@Tag(name = "微信回调接口")
@RestController
//@RequestMapping("wx")
@Slf4j
public class WeComCallbackController {

    @RequestMapping("/wx")
    @PermitAll
    public String verifyCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取请求参数
        String msgSignature = request.getParameter("msg_signature");

        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String echostr = request.getParameter("echostr");

            if (StringUtils.isEmpty(msgSignature)) {
                msgSignature = request.getParameter("signature");
                log.info("入参" + msgSignature + "====" + nonce + "====" + timestamp);

                if (checkSignature(msgSignature, timestamp, nonce)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    log.info("验证成功 ：" + echostr);
                    return echostr;
//                    response.getWriter().write(echostr); // 返回 echostr 完成验证
                } else {
                    log.info("验证失败");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }

            // GET 请求用于验证回调 URL
            if (verifySignature(msgSignature, timestamp, nonce, echostr)) {
                response.setStatus(HttpServletResponse.SC_OK);
                return decryptMessage(echostr);
//                response.getWriter().write(decryptMessage(echostr)); // 返回 echostr 完成验证
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            // POST 请求用于接收和处理消息
            StringBuilder body = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }
            String encryptedXml = body.toString();
            String msg = XmlUtil.xmlToMap(encryptedXml).get("Encrypt").toString();
            try {
//                // 验证签名
                if (!verifySignature(msgSignature, timestamp, nonce, msg)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return "error";
                }

                // 解密消息
                String decryptedMsg = decryptMessage(msg);
                log.info("Decrypted Message: " + decryptedMsg);

                // 处理业务逻辑：根据接收到的消息进行响应

                // 加密被动回复消息
                String encryptedReply = encryptMessage(decryptedMsg, timestamp, nonce, CORP_ID);
                log.info("encryptedReply Message: " + encryptedReply);

                // 回复加密消息
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/xml");
//                response.getWriter().write(encryptedReply);
                return encryptedReply;

            } catch (Exception e) {
                e.printStackTrace();
                log.error("error Message: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return "error";

        }
        return "error";

    }

    private static final String TOKEN = "bkuui6nLzl9e"; // 企业微信后台配置的 Token
    private static final String ENCODING_AES_KEY = "nmgSupsuy3obEDpKmc7ioyvuw8rXT2jqnFsSx6Uzckq"; // 企业微信后台配置的 EncodingAESKey
    private static final String CORP_ID = "ww224258be858951bd"; // 企业微信的 CorpID

    /**
     * 验证消息签名
     *
     * @param msgSignature 从请求中获得的签名
     * @param timestamp    时间戳
     * @param nonce        随机数
     * @param msg          消息内容或 echostr
     * @return true 表示签名合法
     */
    public static boolean verifySignature(String msgSignature, String timestamp, String nonce, String msg) {
        try {
            String computedSignature = generateSignature(TOKEN, timestamp, nonce, msg);
            return computedSignature.equals(msgSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成签名
     *
     * @param msg 消息内容或 echostr
     * @return 签名字符串
     * @throws Exception 生成签名失败时抛出异常
     */

    public static String generateSignature(String token, String timestamp, String nonce, String msg) throws Exception {
        StringBuilder sb = new StringBuilder();

        String[] array = new String[]{token, timestamp, nonce, msg};
        Arrays.sort(array);

        for (String s : array) {
            sb.append(s);
        }

        MessageDigest md = MessageDigest.getInstance("SHA-1");

        byte[] digest = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStr = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                hexStr.append(0);
            }
            hexStr.append(hex);
        }
        return hexStr.toString();
    }

    /**
     * 解密消息（接收消息时）
     *
     * @param encryptedMsg 加密的消息内容
     * @return 解密后的消息内容
     * @throws Exception 解密失败时抛出异常
     */
    public static String decryptMessage(String encryptedMsg) throws Exception {
        // AES 密钥解码
        byte[] aesKey = Base64Utils.decodeFromString(ENCODING_AES_KEY + "=");
        // 解码 Base64 的密文
        byte[] ciphertext = Base64Utils.decodeFromString(encryptedMsg);

        // 初始化 AES 解密
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        // 解密数据
        byte[] original = cipher.doFinal(ciphertext);

        // 解析解密后的内容
        if (original.length <= 20) {
            throw new IllegalArgumentException("Decrypted content is too short to parse.");
        }

        // 去掉前 16 字节随机数据
        byte[] content = new byte[original.length - 16];
        System.arraycopy(original, 16, content, 0, original.length - 16);

        // 提取消息长度（前 4 字节）
        byte[] msgLenBytes = new byte[4];
        System.arraycopy(content, 0, msgLenBytes, 0, 4);
        int msgLen = ByteBuffer.wrap(msgLenBytes).getInt();

        // 检查数据完整性
        if (content.length < msgLen + 4) {
            throw new IllegalArgumentException("Invalid message length.");
        }

        // 提取消息内容
        byte[] msgBytes = new byte[msgLen];
        System.arraycopy(content, 4, msgBytes, 0, msgLen);
        String msg = new String(msgBytes, StandardCharsets.UTF_8);
        return msg.trim();
    }

    /**
     * 加密消息（响应消息时）
     *
     * @param plainText 明文消息
     * @return 加密后的消息内容
     * @throws Exception 加密失败时抛出异常
     */
    public static String encryptMessage(String plainText, String timestamp, String nonce, String receiveId) throws Exception {
        // AES 密钥解码
        byte[] aesKey = Base64Utils.decodeFromString(ENCODING_AES_KEY + "=");

        // 随机生成 16 字节的随机数据
        byte[] randomBytes = new byte[16];
        new java.security.SecureRandom().nextBytes(randomBytes);

        // 消息内容转换为字节数组
        byte[] msgBytes = plainText.getBytes(StandardCharsets.UTF_8);

        // 消息长度转为 4 字节
        int msgLen = msgBytes.length;
        ByteBuffer msgLenBuffer = ByteBuffer.allocate(4).putInt(msgLen);
        byte[] msgLenBytes = msgLenBuffer.array();

        // ReceiveId 转为字节数组
        byte[] receiveIdBytes = receiveId.getBytes(StandardCharsets.UTF_8);

        // 构造 rand_msg = random(16B) + msg_len(4B) + msg + receiveid
        ByteBuffer buffer = ByteBuffer.allocate(randomBytes.length + msgLenBytes.length + msgBytes.length + receiveIdBytes.length);
        buffer.put(randomBytes);       // 添加随机数据
        buffer.put(msgLenBytes);       // 添加消息长度
        buffer.put(msgBytes);          // 添加消息内容
        buffer.put(receiveIdBytes);    // 添加 receiveid
        byte[] randMsg = buffer.array();

        // 对 rand_msg 进行 AES 加密
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        // 补齐数据长度为 16 的倍数
        int blockSize = cipher.getBlockSize();
        int paddedLength = (randMsg.length + blockSize - 1) / blockSize * blockSize;
        byte[] paddedRandMsg = Arrays.copyOf(randMsg, paddedLength);

        // 加密后的数据
        byte[] encrypted = cipher.doFinal(paddedRandMsg);

        // Base64 编码加密后的消息
        String encryptedMsg = Base64Utils.encodeToString(encrypted);

        // 生成消息签名
        String msgSignature = generateSignature(TOKEN, timestamp, nonce, encryptedMsg);

        // 构造返回 XML 格式
        return String.format(
                "<xml><Encrypt>%s</Encrypt><MsgSignature>%s</MsgSignature><TimeStamp>%s</TimeStamp><Nonce>%s</Nonce></xml>",
                encryptedMsg, msgSignature, timestamp, nonce
        );
    }

    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        try {
            // 1. 将 token、timestamp、nonce 按字典序排序
            String[] arr = {TOKEN, timestamp, nonce};
            Arrays.sort(arr);
            // 2. 将排序后的结果拼接成一个字符串
            StringBuilder content = new StringBuilder();
            for (String str : arr) {
                content.append(str);
            }
            log.info("排序结果：" + content);


            // 3. 进行 SHA1 加密
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());

            // 4. 将加密后的字节数组转成十六进制字符串
            StringBuilder hexStr = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(hex);
            }

            // 5. 与 signature 对比，返回验证结果
            return hexStr.toString().equals(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
