package cn.wenxun.admin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LongestHighlightExtractor {
    public static String HighlightExtractor(String inputText) {
        try {


            // 正则表达式匹配 <span> 标签中的内容
            String regex = "<span[^>]*>(.*?)</span>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(inputText);

            String longestText = "";
            int longestStartIndex = -1;
            int longestEndIndex = -1;

            // 查找所有高亮部分
            while (matcher.find()) {
                String highlightedText = matcher.group(1);
                int startIndex = matcher.start();
                int endIndex = matcher.end();

                // 如果当前高亮部分比之前的最长部分更长，更新最长文本的信息
                if (highlightedText.length() > longestText.length()) {
                    longestText = highlightedText;
                    longestStartIndex = startIndex;
                    longestEndIndex = endIndex;
                }
            }

            // 如果找到了最长的高亮文本
            if (!longestText.isEmpty()) {
                // 获取最长高亮部分的前后50个字
                String before = extractContext(inputText, longestStartIndex, false);
                String after = extractContext(inputText, longestEndIndex, true);

                // 输出前后50个字及最长高亮部分
                return (before + "<span class=\"highlight\">" + longestText + "</span>" + after);
            } else {
//                String before = extractContext(inputText, 0, false);
                String after = extractContext(inputText, 0, true);

                // 输出前后50个字及最长高亮部分
                return (longestText + after);
            }
        } catch (Exception e) {
            return inputText;
        }
    }

    // 提取上下文，确保不拆分标签
    private static String extractContext(String text, int index, boolean isAfter) {
        int start = isAfter ? index : Math.max(0, index - 100);
        int end = isAfter ? Math.min(text.length(), index + 100) : index;

        if (start < 100) {
            end = isAfter ? Math.min(text.length(), index + 200 - start) : index;
        }
        end = Math.max(text.indexOf('。', end), end);

        String context = text.substring(start, end);


        // 如果前面有更多内容，用 "..." 代替
        if (!isAfter && start > 0) {
            context = "..." + context;
        }
        // 如果后面有更多内容，用 "..." 代替
        if (isAfter && end < text.length()) {
            context = context + "...";
        }

        return context;
    }

    public static void main(String[] args) {
        String inputText = " 10月31日上午，滨州职业学院组织第九次党委理论学习中心组集体学习研讨，专题学习习近平法治思想，党委书记刘荩一主持并讲话，党委理论学习中心组成员参加学习。刘荩一强调，习近平法治思想为推进全面依法治国提供了根本遵循和行动指南，要学深悟透笃行，切实将其贯彻到依法治校全过程各方面，以实际行动坚定拥护“两个确立”、坚决做到“两个维护”，为学校跨越式高质量发展提供坚实保障。要在“深学”上用心用力，强化使命担当，要吃透基本精神。";

        String result = HighlightExtractor(inputText);

        System.out.println(result);
    }
}
