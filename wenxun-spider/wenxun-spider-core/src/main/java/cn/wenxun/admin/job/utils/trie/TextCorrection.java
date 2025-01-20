package cn.wenxun.admin.job.utils.trie;

import org.springframework.util.CollectionUtils;

import java.util.*;

public class TextCorrection {

    // 获取某个词在文本中的所有起始和结束坐标
    public static List<int[]> findWordCoordinates(String text, String word) {
        List<int[]> coordinates = new ArrayList<>();
        if (text == null || word == null || word.isEmpty()) {
            throw new IllegalArgumentException("文本或错词不能为空");
        }
        int index = text.indexOf(word);
        while (index != -1) {
            coordinates.add(new int[]{index, index + word.length()});
            index = text.indexOf(word, index + 1);
        }
        return coordinates;
    }

    // 判断一个错词的所有坐标是否完全包含在另一个错词的坐标中
    public static List<int[]> areAllCoordinatesContainedInCoordinates(List<int[]> longWordCoordinates, List<int[]> shortWordCoordinates) {
        List<int[]> newCoordinates = new ArrayList<>();
        for (int[] shortWordCoordinate : shortWordCoordinates) {
            boolean isContained = false;
            for (int[] longCoordinate : longWordCoordinates) {
                // 判断短词的每个坐标是否被长词的坐标完全包含
                if (shortWordCoordinate[0] >= longCoordinate[0] && shortWordCoordinate[1] <= longCoordinate[1]) {
                    isContained = true;
                    break;
                }
            }
            if (!isContained) {
                newCoordinates.add(shortWordCoordinate);
            }
        }

        return newCoordinates;
    }

    public static Map<String, List<int[]>> cleanBmap(Map<String, List<int[]>> Amap, Map<String, List<int[]>> Bmap, int type) {
        // 遍历 Bmap 的每个键值对
        Map<String, List<int[]>> newMap = new HashMap<>();
        Bmap.forEach((bkey, bCoordinates) -> {

            // 检查 Amap 中是否包含当前的 bkey
            for (String s : Amap.keySet()) {
                if (s.contains(bkey)) {
                    if (type == 2) {
                        if (!s.equals(bkey)) {
                            List<int[]> aCoordinates = Amap.get(s);
                            // 对 Bmap 中的坐标列表进行清理
                            bCoordinates = areAllCoordinatesContainedInCoordinates(aCoordinates, bCoordinates);
                        }
                    } else {
                        List<int[]> aCoordinates = Amap.get(s);
                        // 对 Bmap 中的坐标列表进行清理
                        bCoordinates = areAllCoordinatesContainedInCoordinates(aCoordinates, bCoordinates);
                    }

                }
            }
            if (!CollectionUtils.isEmpty(bCoordinates)) {
                newMap.put(bkey, bCoordinates);

            }
        });

        return newMap;
    }

    // 处理错词集合，剔除符合条件的错词
    public static Set<String> filterWrongWords(String text, Set<String> wrongWords, Set<String> correctWords) {
        if (text == null || wrongWords == null || correctWords == null) {
            throw new IllegalArgumentException("文本、错词集合或正确词集合不能为空");
        }
        if (wrongWords.isEmpty()) {
            return Collections.emptySet();  // 如果错词集合为空，直接返回空集合
        }

        // 存储错词的坐标集合
        Map<String, List<int[]>> wrongWordCoordinatesMap = new HashMap<>();
        for (String wrongWord : wrongWords) {
            List<int[]> coordinates = findWordCoordinates(text, wrongWord);
            if (!coordinates.isEmpty()) {
                wrongWordCoordinatesMap.put(wrongWord, coordinates);
            }
        }

        // 存储正确词的坐标集合
        Map<String, List<int[]>> correctWordCoordinatesMap = new HashMap<>();
        for (String correctWord : correctWords) {
            List<int[]> coordinates = findWordCoordinates(text, correctWord);
            if (!coordinates.isEmpty()) {
                correctWordCoordinatesMap.put(correctWord, coordinates);
            }
        }

        wrongWordCoordinatesMap = cleanBmap(correctWordCoordinatesMap, wrongWordCoordinatesMap, 1);
        wrongWordCoordinatesMap = cleanBmap(wrongWordCoordinatesMap, wrongWordCoordinatesMap, 2);

        return wrongWordCoordinatesMap.keySet();
    }


    public static void main(String[] args) {
        try {
            String text = "你好世界，这里有错词的错误的人生的不是错误";
            Set<String> wrongWords = new HashSet<>(Arrays.asList("错误1", "错误的人生", "好世界"));
            Set<String> correctWords = new HashSet<>(Arrays.asList("世界",  "不是错词"));

            Set<String> resultWrongWords = filterWrongWords(text, wrongWords, correctWords);
            System.out.println("剩余的错词集合: " + resultWrongWords);
        } catch (IllegalArgumentException e) {
            System.err.println("输入参数错误: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("发生异常: " + e.getMessage());
        }
    }
}
