package cm.iocoder.captcha.model.common;

/**
 * 底图类型枚举
 */
public enum CaptchaBaseMapEnum {
    /**
     * 旋转拼图底图
     */
    ROTATE("ROTATE", "旋转拼图底图"),
    /**
     * 旋转拼图旋转块底图
     */
    ROTATE_BLOCK("ROTATE_BLOCK", "旋转拼图旋转块底图"),
    /**
     * 滑动拼图底图
     */
    ORIGINAL("ORIGINAL", "滑动拼图底图"),
    /**
     * 滑动拼图滑块底图
     */
    SLIDING_BLOCK("SLIDING_BLOCK", "滑动拼图滑块底图"),
    /**
     * 文字点选底图
     */
    PIC_CLICK("PIC_CLICK", "文字点选底图");

    private final String codeValue;
    private final String codeDesc;

    private CaptchaBaseMapEnum(String codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }

    public String getCodeValue() {
        return this.codeValue;
    }

    public String getCodeDesc() {
        return this.codeDesc;
    }

    /**
     * 根据codeValue获取枚举
     *
     * @param codeValue 验证值
     * @return 枚举
     */
    public static CaptchaBaseMapEnum parseFromCodeValue(String codeValue) {
        for (CaptchaBaseMapEnum e : CaptchaBaseMapEnum.values()) {
            if (e.codeValue.equals(codeValue)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据codeValue获取描述
     *
     * @param codeValue 验证值
     * @return 描述
     */
    public static String getCodeDescByCodeBalue(String codeValue) {
        CaptchaBaseMapEnum enumItem = parseFromCodeValue(codeValue);
        return enumItem == null ? "" : enumItem.getCodeDesc();
    }

    /**
     * 验证codeValue是否有效
     *
     * @param codeValue 验证值
     * @return 是否有效
     */
    public static boolean validateCodeValue(String codeValue) {
        return parseFromCodeValue(codeValue) != null;
    }

    /**
     * 列出所有值字符串
     *
     * @return 所有值字符串
     */
    public static String getString() {
        StringBuilder buffer = new StringBuilder();
        for (CaptchaBaseMapEnum e : CaptchaBaseMapEnum.values()) {
            buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString().trim();
    }

}
