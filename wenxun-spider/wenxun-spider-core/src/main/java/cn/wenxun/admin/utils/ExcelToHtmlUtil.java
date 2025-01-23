package cn.wenxun.admin.utils;

import com.aspose.cells.*;

import java.io.*;

/**
 * excel转换为html的工具类
 * <p>
 * 无私的小伙伴儿们
 * https://gitee.com/buji-qiang/pdf-word-image
 * https://gitee.com/guo-zhongtian/convert-excel-to-pdf
 *
 * @author yz
 * @date 2024/01/22
 */
public class ExcelToHtmlUtil {

    /**
     * 许可证字符串
     */
    private static final String LICENSE_STR = "<License> " +
            "  <Data>" +
            "    <Products>" +
            "      <Product>Aspose.Total for Java</Product>" +
            "      <Product>Aspose.Words for Java</Product>" +
            "    </Products>" +
            "    <EditionType>Enterprise</EditionType>" +
            "    <SubscriptionExpiry>20991231</SubscriptionExpiry>" +
            "    <LicenseExpiry>20991231</LicenseExpiry>" +
            "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>" +
            "  </Data>" +
            "  <Signature>" +
            "    sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppod0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=" +
            "  </Signature>" +
            "</License>";
//
//            "<License>" +
//            "<Data>" +
//            "<Products><Product>Aspose.Total for Java</Product><Product>Aspose.Cells for Java</Product></Products>" +
//            "<EditionType>Enterprise</EditionType>" +
//            "<SubscriptionExpiry>20991231</SubscriptionExpiry>" +
//            "<LicenseExpiry>20991231</LicenseExpiry>" +
//            "<SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>" +
//            "</Data>" +
//            "<Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>" +
//            "</License>";

    /**
     * excel 转 html
     *
     * @param excelFilePath excel文件路径
     * @return 返回转换后的html内容
     */
    public static String convert(InputStream excelFilePath) {
        return convert(excelFilePath, null);
    }

    /**
     * excel 转 html
     *
     * @param excelFilePath excel文件路径
     * @param convertSheets 需要转换的sheet
     * @return 返回转换后的html内容
     */
    public static String convert(InputStream excelFilePath, int[] convertSheets) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // 设置License
            setLicense();
            // 读取excel文件
            Workbook wb = new Workbook(excelFilePath);

            // 获取工作簿中的所有工作表
            WorksheetCollection worksheets = wb.getWorksheets();
            if (worksheets.getCount() > 1) {


                // 创建一个新的工作表用于合并结果
                Worksheet mergedSheet = worksheets.add("MergedSheet");

                // 获取源工作表列表（可以自定义要合并的工作表范围）
                int sheetCount = worksheets.getCount();
                int startRow = 0;

                // 遍历每个工作表并复制内容
                for (int i = 0; i < sheetCount; i++) {
                    if (worksheets.get(i).getName().equals("MergedSheet")) {
                        continue;  // 跳过已经是目标工作表的合并工作表
                    }

                    Worksheet sheet = worksheets.get(i);
                    copySheetContent(sheet, mergedSheet, startRow);

                    // 更新 startRow，以便将下一工作表内容追加到合适的行
                    startRow = mergedSheet.getCells().getMaxDataRow() + 1;
                }


                // 删除除合并工作表外的其他工作表
                for (int i = sheetCount - 1; i >= 0; i--) {
                    Worksheet sheet = worksheets.get(i);
                    if (!sheet.getName().equals("MergedSheet")) {
                        worksheets.removeAt(i);  // 删除工作表
                    }
                }
            }
            // 设置图片保存的目录
            String imagesFolder = System.getProperty("java.io.tmpdir") + "/excel_images"; // 临时目录
            File imageFolder = new File(imagesFolder);
            if (!imageFolder.exists()) {
                imageFolder.mkdirs(); // 如果文件夹不存在则创建
            }

            // 设置html保存选项
            HtmlSaveOptions options = new HtmlSaveOptions(SaveFormat.HTML);
            options.setExportFrameScriptsAndProperties(false);
            options.setExportActiveWorksheetOnly(false);

            if (convertSheets != null) {
                for (int sheetIndex : convertSheets) {
                    wb.getWorksheets().get(sheetIndex).setVisible(true);
                }
            } else {
                // 显示所有sheet
                for (int i = 0; i < wb.getWorksheets().getCount(); i++) {
                    wb.getWorksheets().get(i).setVisible(true);
                }
            }

            // 保存到流
            wb.save(byteArrayOutputStream, options);
            return byteArrayOutputStream.toString("UTF-8");  // 返回转换后的 HTML 字符串
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

    // 复制工作表内容到目标工作表
    private static void copySheetContent(Worksheet sourceSheet, Worksheet targetSheet, int startRow) {
        Cells sourceCells = sourceSheet.getCells();
        Cells targetCells = targetSheet.getCells();

        // 获取源工作表的最大行和列
        int maxRow = sourceCells.getMaxDataRow();
        int maxCol = sourceCells.getMaxDataColumn();

        // 遍历并复制源工作表的内容
        for (int row = 0; row <= maxRow; row++) {
            for (int col = 0; col <= maxCol; col++) {
                // 获取源单元格的值并复制到目标单元格
                targetCells.get(row + startRow, col).setValue(sourceCells.get(row, col).getStringValue());
            }
        }
    }

    /**
     * 设置 license 去除水印
     */
    private static void setLicense() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(LICENSE_STR.getBytes());
        License license = new License();
        license.setLicense(byteArrayInputStream);
    }

    public static void main(String[] args) throws FileNotFoundException {
        String path = "C:\\Users\\wangjp\\Desktop\\QD24H2-183489需更新裁判文书维度的企业名单.xlsx";
        InputStream inputStream = new FileInputStream(path);
        // 测试代码
        String htmlContent = ExcelToHtmlUtil.convert(inputStream);
        System.out.println(htmlContent);
    }
}
