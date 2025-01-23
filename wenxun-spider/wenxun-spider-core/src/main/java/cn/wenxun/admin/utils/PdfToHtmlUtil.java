package cn.wenxun.admin.utils;


import com.aspose.pdf.Document;
import com.aspose.pdf.HtmlSaveOptions;
import com.aspose.pdf.LettersPositioningMethods;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * pdf 转 html 的工具类
 *
 * @author yz
 * @date 2024/01/22
 */
public class PdfToHtmlUtil {

    /**
     * 将 PDF 文件转换为 HTML 字符串
     *
     * @param pdfFilePath PDF 文件路径
     * @return 转换后的 HTML 字符串
     */

    public static String PDFtoHTMLStream(InputStream pdfFilePath) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // 加载 PDF 文件
            Document doc = new Document(pdfFilePath);

            // 调整转换参数
            HtmlSaveOptions newOptions = new HtmlSaveOptions();

            // 设置图像保存模式为嵌入到 HTML 背景中
            newOptions.setRasterImagesSavingMode(HtmlSaveOptions.RasterImagesSavingModes.AsEmbeddedPartsOfPngPageBackground);

            // 设置字体保存模式
            newOptions.setFontSavingMode(HtmlSaveOptions.FontSavingModes.SaveInAllFormats);

            // 设置嵌入模式，将所有部分嵌入 HTML 中
            newOptions.setPartsEmbeddingMode(HtmlSaveOptions.PartsEmbeddingModes.EmbedAllIntoHtml);

            // 设置字母定位方法，使用 Em 单位和 CSS 中的舍入误差补偿
            newOptions.setLettersPositioningMethod(LettersPositioningMethods.UseEmUnitsAndCompensationOfRoundingErrorsInCss);

            // 不拆分为多个 HTML 页面
            newOptions.setSplitIntoPages(false);

            // 自定义 HTML 保存策略
            newOptions.setCustomHtmlSavingStrategy(new HtmlSaveOptions.HtmlPageMarkupSavingStrategy() {

                @Override
                public void invoke(HtmlSaveOptions.HtmlPageMarkupSavingInfo htmlSavingInfo) {
                }
            });


            // 调用保存方法
            doc.save(byteArrayOutputStream, newOptions);
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

//    private static String imagesFolder = System.getProperty("java.io.tmpdir") + "images"; // 临时目录

    public static void main(String[] args) throws IOException {
        // 测试代码
//        String htmlContent = PdfToHtmlUtil.PDFtoHTMLStream("C:\\Users\\wangjp\\Desktop\\送达公告附件.pdf");
//        System.out.println(htmlContent);  // 打印转换后的 HTML 内容
    }
}
