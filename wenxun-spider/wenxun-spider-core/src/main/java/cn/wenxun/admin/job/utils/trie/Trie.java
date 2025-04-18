package cn.wenxun.admin.job.utils.trie;

import cn.hutool.core.util.CharUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.wenxunDict.vo.data.WenXunDictDataPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.service.wenxunDict.WenXunDictDataService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Trie implements ApplicationListener<ContextRefreshedEvent> {

    public static final TrieNode root = new TrieNode(); // 存储无意义字符
    private final transient ReentrantLock writeLock = new ReentrantLock();
    @Resource
    private WenXunDictDataService wenXunDictDataService;


    public Trie() {

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializeSensitiveWords();
    }

    /**
     * 初始化敏感词
     */
    private void initializeSensitiveWords() {
        try {
            long start = System.currentTimeMillis();
            WenXunDictDataPageReqVO vo = new WenXunDictDataPageReqVO();
            vo.setStatus(0);
            vo.setPageNo(1);
            vo.setPageSize(500);
            PageResult<WenXunDictDataDO> pageResult = wenXunDictDataService.getDictDataPage(vo);

            if (pageResult.getTotal() > vo.getPageSize()) {
                int totalPage = (int) (pageResult.getTotal() / vo.getPageSize()) + 1;
                for (int i = 1; i <= totalPage; i++) {
                    vo.setPageNo(i);
                    pageResult = wenXunDictDataService.getDictDataPage(vo);
                    for (WenXunDictDataDO wenXunDictDataDO : pageResult.getList()) {
                        if (!wenXunDictDataDO.getDictType().equals("wrong_word_configuration")) {
                            if (StringUtils.isNotEmpty(wenXunDictDataDO.getValue())) {
                                insert(wenXunDictDataDO.getValue());
                            }
                        }

                        if (StringUtils.isNotEmpty(wenXunDictDataDO.getLabel())) {
                            insert(wenXunDictDataDO.getLabel());
                        }
                        if (StringUtils.isNotEmpty(wenXunDictDataDO.getRemark())) {
                            String[] remark = StringUtils.split(wenXunDictDataDO.getRemark(), "|");
                            for (String s : remark) {
                                insert(s);
                            }
                        }

                    }
                }
            }
            System.out.println("前缀树构建耗时: " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 往 Trie 树中插入一个字符串
    public void insert(String s) {
        TrieNode p = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (p.children.get(c) == null)
                p.children.put(c, new TrieNode(c));
            p = p.children.get(c);
        }
        p.isEndingChar = true;
    }

    // 在 Trie 树中查找有没有s这个字符串
    private boolean search(String s) {
        TrieNode p = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (p.children.get(c) == null)
                return false;
            p = p.children.get(c);
        }
        // p.isEndingChar = false 时, 表示不能完全匹配，只是前缀
        // 为 true 时, 表示找到 pattern
        return p.isEndingChar;
    }

    // 查找文本中所有的敏感词并返回匹配的词组
    public Set<String> getMatchingWords(String text) {
        Set<String> matchedWords = new HashSet<>();
        TrieNode p;
        for (int i = 0; i < text.length(); i++) {
            p = root;
            int j = i;
            String attr = "";
            while (j < text.length()) {
                try {

                    char c = text.charAt(j);
                    if (p.data != null) {
                        attr = attr + p.data.toString();
                    }
                    p = p.children.get(c);
                    if (p == null) {
                        break;
                    }
                    if (p.isEndingChar) {
                        matchedWords.add(attr + p.data.toString());
                    }
                    j++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return matchedWords;
    }

    // 获取当前节点及所有子节点组成的完整字符串
    private String getFullWord(TrieNode node) {
        StringBuilder fullWord = new StringBuilder();
        collectWords(node, fullWord);
        return fullWord.toString();
    }

    // 递归收集所有子节点的词
    private void collectWords(TrieNode node, StringBuilder result) {
        if (node.isEndingChar) {
            result.append(node.data).append("");
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            collectWords(entry.getValue(), result);
        }
    }

    /**
     * 热更新
     *
     * @param words 待新增或待删除的敏感词条
     * @param save  true 为新增，false 为删除
     */
    public final void refresh(String[] words, boolean save) {
        // 1. cow
        TrieNode p = root, oldRoot = p;

        if (!save) {
            // delete
            for (String word : words) {
                // 此时 newNode 完全是 root 的副本, root 没有, newNode 自然不可能有
                boolean find = search(word);
                if (!find) continue;

                // do delete
                for (char c : word.toCharArray())
                    p = p.children.get(c);
                p.isEndingChar = false; // 写操作原子
            }
            return;
        }

        //doAdd(p, words);
        addToTrie(root, words);
    }

    // 分段锁来做, 不需要再更新引用做拷贝, 直接在原树上分段锁加
    private final void addToTrie(TrieNode root, String[] words) {
        for (String word : words) {
            if (word == null || word.length() == 0) continue;
            Character firstChar = word.charAt(0);
            retry:
            for (; ; ) {
                if (root.children.get(firstChar) == null) {
                    TrieNode node = new TrieNode(word.charAt(0));
                    writeLock.lock();   // 锁 null, 这里仍然会存在写竞争
                    try {
                        if (root.children.get(firstChar) != null)   // double check
                            continue retry;
                        root.children.put(firstChar, node);
                    } finally {
                        writeLock.unlock();
                    }
                    // 不管成功没成功, 都 retry
                    continue retry;
                } else {
                    // 正常分段锁
                    int n = word.length(), idx = 1;
                    TrieNode p = root, tmp = p.children.get(firstChar);
                    while (tmp != null) {
                        TrieNode t = tmp;
                        tmp = tmp.children.get(idx++);
                        p = t;
                    }

                    synchronized (p) {
                        while (idx < n) {
                            char c = word.charAt(idx++);
                            p.children.put(c, new TrieNode(c));
                            p = p.children.get(c);
                        }
                    }
                    break;
                }
            }
        }
    }


    // 并发不如分段锁
    @Deprecated
    private final void doAdd(TrieNode p, String[] words) {
        // 后序遍历做拷贝, 写写互斥
        writeLock.lock();
        try {
            TrieNode newNode = COWAndUpdateRoot(p);  // 在这里重入, 复制要复制最新的, 所以和写也要互斥, 否则可能会丢失更新

            for (String s : words) {
                p = newNode;
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (p.children.get(c) == null)
                        p.children.put(c, new TrieNode(c));
                    p = p.children.get(c);
                }
                p.isEndingChar = true;
            }

            p = newNode;
        } finally {
            writeLock.unlock();
        }
    }

    // cow
    private final TrieNode COWAndUpdateRoot(TrieNode root) {
        TrieNode snapshot;
        if ((snapshot = root) == null) return null;
        TrieNode trieNode = new TrieNode();
        trieNode.setEndingChar(snapshot.isEndingChar);
        trieNode.setData(snapshot.data);
        Map<Character, TrieNode> children = snapshot.children;
        // base case
        if (isLeaf(snapshot)) {
            trieNode.setChildren(new HashMap<>(0));
            return trieNode;
        }
        Map<Character, TrieNode> newMap = new HashMap<>(children.size() * 4 / 3 + 1);
        for (Character key : children.keySet())
            newMap.put(key, COWAndUpdateRoot(children.get(key)));
        trieNode.setChildren(newMap);
        return trieNode;
    }

    private boolean isLeaf(TrieNode node) {
        return node.children.size() == 0;
    }

    /**
     * @param text   待过滤文本
     * @param greedy 是否贪婪匹配, 否则默认密集匹配
     * @return 过滤后的文本, 敏感词默认替换为*
     */
    public String filter(String text, boolean greedy) {
        if (greedy) return greedyFilter(text);
        return destinyFilter(text);
    }

    public String filter(String text) {
        return this.filter(text, true);
    }

    // 贪婪匹配
    private String greedyFilter(String text) {
        if (text == null || text.length() == 0)
            return text;
        int i = 0, j = 0;
        StringBuilder sb = new StringBuilder();
        Deque<Frame> stack = new ArrayDeque<>();
        // 读全部基于快照读, 避免在过滤的过程中先读到旧的, 然后写后读到新的
        TrieNode p = root, snapshot = p;
        while (i < text.length()) {
            if (j >= text.length()) j = text.length() - 1;
            char c = text.charAt(j);
            // 跳过符号
            if (isSymbol(c)) {
                if (p == snapshot) {
                    sb.append(c);
                    i++;
                }
                j++;
                continue;
            }
            p = p.children.get(c);
            if (p == null) {
                // 失配
                Frame top = null;
                // 回退到上个匹配点
                //while (!stack.isEmpty() && !(top = stack.pop()).node.isEndingChar) ;
                while (!stack.isEmpty()) {
                    top = stack.pop();
                    if (top.node.isEndingChar) {
                        stack.clear();
                        break;
                    }
                }
                if (top == null || !top.node.isEndingChar) {
                    // 说明该条路径上没有能匹配的
                    sb.append(text.charAt(i));
                    p = snapshot;
                    j = ++i;
                } else {
                    for (int k = i; k <= top.j; k++)
                        sb.append("*");
                    j = top.j;
                    i = ++j;
                    p = snapshot;
                }
            } else {
                // 匹配上
                stack.push(new Frame(p, j));
                j++;
            }
        }
        return sb.toString();
    }

    // 密集匹配
    private String destinyFilter(String text) {
        if (text == null || text.length() == 0)
            return text;
        int i = 0, j = 0;
        StringBuilder sb = new StringBuilder();
        TrieNode p = root, snapshot = p;
        while (j < text.length()) {
            char c = text.charAt(j);

            // 跳过符号
            if (isSymbol(c)) {
                if (p == snapshot) {
                    sb.append(c);
                    i++;
                }
                j++;
                continue;
            }

            p = p.children.get(c);
            if (p == null) {
                // 中途遇到没匹配上的节点
                // 只能判断以 i 开头到 j 的字符串不是敏感词
                // 不是的话只能确定 i 一定不是, 但是 i + 1 ~ j 不一定不是
                sb.append(text.charAt(i));
                p = snapshot;
                // 来到 i 的下一位继续比
                j = ++i;
            } else if (p.isEndingChar) {
                // 匹配到叶子, 并且是敏感词
                //sb.append(REPLACEMENT);	// 这里是全部替换为 ***
                for (int k = i; k <= j; k++)    // 这里是按照长度做替换
                    sb.append("*");
                i = ++j;
                p = snapshot;
            } else {
                // 正常情况匹配上了, 但还不是叶子, 需要继续匹配
                j++;
            }

            // 有可能有的敏感词比较短, 此时被包含在 i, j 中
            if (j == text.length() && i != j) {
                sb.append(text.charAt(i));
                j = ++i;
                p = snapshot;
            }
        }
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF为东亚的文字范围
        return !CharUtil.isLetterOrNumber(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    // 查询一个单词是否是别的单词的前缀
    private boolean prefix(String s) {
        TrieNode p = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (p.children.get(c) == null)
                return false;
            p = p.children.get(c);
        }
        return true;
    }

    // 查询一个单词有多少个单词前缀
    private int numOfPrefix(String s) {
        int num = 0;
        TrieNode p = root;
        for (int i = 0; i < s.length(); i++) {
            p = root.children.get(s.charAt(i));
            if (p == null) break;
            if (p.isEndingChar) num++;
        }
        return num;
    }
}
