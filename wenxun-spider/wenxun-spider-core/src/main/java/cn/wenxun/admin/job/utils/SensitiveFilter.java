package cn.wenxun.admin.job.utils;


import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataService;
import cn.wenxun.admin.job.utils.trie.Trie;

import java.util.Set;

public class SensitiveFilter {

    static Trie trie = new Trie();

    public static String filter(String text, boolean greedy) {
        return trie.filter(text, greedy);
    }

    public static void delete(String... words) {
        trie.refresh(words, false);
    }

    public static String filter(String text) {

        return trie.filter(text);
    }

    public static Set<String> getMatchingWords(String text, WenXunDictDataService wenXunDictDataService1) {

        return trie.getMatchingWords(text);
    }
}
