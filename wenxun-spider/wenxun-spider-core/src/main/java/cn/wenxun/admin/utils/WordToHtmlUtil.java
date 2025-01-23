package cn.wenxun.admin.utils;

import com.aspose.words.Document;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.License;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * word转换为html的工具类
 * <p>
 * 无私的小伙伴儿们
 * https://gitee.com/buji-qiang/pdf-word-image
 * https://gitee.com/guo-zhongtian/convert-excel-to-pdf
 *
 * @author yz
 * @date 2024/01/22
 */
public class WordToHtmlUtil {

    /**
     * 许可证字符串
     */
    private static final String LICENSE_STR = "<License>" +
            "<Data>" +
            "<Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products>" +
            "<EditionType>Enterprise</EditionType>" +
            "<SubscriptionExpiry>20991231</SubscriptionExpiry>" +
            "<LicenseExpiry>20991231</LicenseExpiry>" +
            "<SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>" +
            "</Data>" +
            "<Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>" +
            "</License>";

    /**
     * /**
     * word 转 html
     *
     * @param wordFilePath word文件路径
     * @return 返回转换后的html内容
     */
    public static String convert(InputStream wordFilePath) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            setLicense();
            Document doc = new Document(wordFilePath);

            // 设置图片保存的目录
            String imagesFolder = System.getProperty("java.io.tmpdir") + "images"; // 临时目录
            File imageFolder = new File(imagesFolder);
            if (!imageFolder.exists()) {
                imageFolder.mkdirs(); // 如果文件夹不存在则创建
            }

            HtmlSaveOptions options = new HtmlSaveOptions();
            options.setImagesFolder(imagesFolder);  // 设置图片保存目录
            options.setImagesFolderAlias("images"); // 设置图片 URL 别名

            doc.save(byteArrayOutputStream, options); // 使用HtmlSaveOptions保存为HTML

            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);  // 返回转换后的 HTML 字符串
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置 license 去除水印
     */
    private static void setLicense() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(LICENSE_STR.getBytes());
        License license = new License();
        try {
            license.setLicense(byteArrayInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        System.out.println(convert("C:\\Users\\wangjp\\Desktop\\一种企业信息驱动的个人商业履历生成的方法.doc"));
    }

}
