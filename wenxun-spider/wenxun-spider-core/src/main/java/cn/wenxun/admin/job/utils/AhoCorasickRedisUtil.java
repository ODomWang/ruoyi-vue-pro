package cn.wenxun.admin.job.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.*;

public class AhoCorasickRedisUtil {
    private static class TrieNode {
        Map<Character, String> children = new HashMap<>(); // 子节点，保存子节点的ID
        String failureLink = null; // 失败指针的ID
        List<String> output = new ArrayList<>(); // 记录匹配到的模式串
    }

    private final StringRedisTemplate stringRedisTemplate;
    private final String ROOT_NODE_ID = "root";

    public AhoCorasickRedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 将节点信息存储到 Redis 中
    private void storeNodeToRedis(String nodeId, TrieNode node) {
        String key = "trie:" + nodeId;
        Map<String, String> hash = new HashMap<>();

        // 存储子节点
        for (Map.Entry<Character, String> entry : node.children.entrySet()) {
            hash.put("child:" + entry.getKey(), entry.getValue());
        }

        // 存储失败指针
        if (node.failureLink != null) {
            hash.put("failureLink", node.failureLink);
        }

        // 存储输出列表
        if (!node.output.isEmpty()) {
            hash.put("output", String.join(",", node.output));
        }
        if (!hash.isEmpty()) {
            stringRedisTemplate.opsForHash().putAll(key, hash);
        }
    }

    // 添加敏感词到 Trie 树并存储到 Redis 中
    public void addPattern(String pattern) {
        // 如果 Redis 中已存在相同的敏感词，则不再缓存
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("trie:" + pattern))) {
            return;
        }


        String currentNodeId = ROOT_NODE_ID;

        for (char ch : pattern.toCharArray()) {
            String key = "trie:" + currentNodeId;
            String childNodeId = (String) stringRedisTemplate.opsForHash().get(key, "child:" + ch);

            if (childNodeId == null) {
                // 创建新的节点
                childNodeId = UUID.randomUUID().toString();
                TrieNode newNode = new TrieNode();
                storeNodeToRedis(childNodeId, newNode);

                // 更新当前节点的子节点信息到 Redis
                stringRedisTemplate.opsForHash().put(key, "child:" + ch, childNodeId);
            }
            currentNodeId = childNodeId;
        }

        // 标记该节点为敏感词结束
        String finalKey = "trie:" + currentNodeId;
        stringRedisTemplate.opsForHash().put(finalKey, "output", pattern);
    }

    // 添加敏感词组到 Redis 中
    public void addPatternGroup(String groupName, List<String> patterns) {
        String groupKey = "sensitive:groups";
        // 如果 Redis 中已存在相同的敏感词组，则不再缓存
        if (stringRedisTemplate.opsForHash().hasKey(groupKey, groupName)) {
            return;
        }
        stringRedisTemplate.opsForHash().put(groupKey, groupName, String.join(",", patterns));
    }

    // 构建失败指针并存储到 Redis 中
    public void buildFailureLinks() {
        Queue<String> queue = new LinkedList<>();
        // 初始化根节点子节点的失败指针为根节点
        for (String childId : getChildren(ROOT_NODE_ID)) {
            stringRedisTemplate.opsForHash().put("trie:" + childId, "failureLink", ROOT_NODE_ID);
            queue.add(childId);
        }

        // BFS 构建失败指针
        while (!queue.isEmpty()) {
            String currentNodeId = queue.poll();
            TrieNode currentNode = loadNodeFromRedis(currentNodeId);

            for (Map.Entry<Character, String> entry : currentNode.children.entrySet()) {
                char ch = entry.getKey();
                String childNodeId = entry.getValue();

                // 查找失败指针
                String failNodeId = (String) stringRedisTemplate.opsForHash().get("trie:" + currentNodeId, "failureLink");
                while (failNodeId != null && !stringRedisTemplate.opsForHash().hasKey("trie:" + failNodeId, "child:" + ch)) {
                    failNodeId = (String) stringRedisTemplate.opsForHash().get("trie:" + failNodeId, "failureLink");
                }

                if (failNodeId != null) {
                    stringRedisTemplate.opsForHash().put("trie:" + childNodeId, "failureLink", (String) stringRedisTemplate.opsForHash().get("trie:" + failNodeId, "child:" + ch));
                } else {
                    stringRedisTemplate.opsForHash().put("trie:" + childNodeId, "failureLink", ROOT_NODE_ID);
                }

                // 继承失败指针的输出
                String failureLinkNodeId = (String) stringRedisTemplate.opsForHash().get("trie:" + childNodeId, "failureLink");
                if (failureLinkNodeId != null) {
                    String failureOutput = (String) stringRedisTemplate.opsForHash().get("trie:" + failureLinkNodeId, "output");
                    if (failureOutput != null) {
                        stringRedisTemplate.opsForHash().put("trie:" + childNodeId, "output", failureOutput);
                    }
                }

                queue.add(childNodeId);
            }
        }
    }

    // 从 Redis 中加载节点信息
    private TrieNode loadNodeFromRedis(String nodeId) {
        String key = "trie:" + nodeId;
        Map<Object, Object> hash = stringRedisTemplate.opsForHash().entries(key);

        TrieNode node = new TrieNode();
        for (Map.Entry<Object, Object> entry : hash.entrySet()) {
            String field = (String) entry.getKey();
            String value = (String) entry.getValue();

            if (field.startsWith("child:")) {
                char ch = field.charAt(6);
                node.children.put(ch, value);
            } else if (field.equals("failureLink")) {
                node.failureLink = value;
            } else if (field.equals("output")) {
                node.output = Arrays.asList(value.split(","));
            }
        }
        return node;
    }

    // 获取节点的所有子节点 ID
    private List<String> getChildren(String nodeId) {
        String key = "trie:" + nodeId;
        List<String> children = new ArrayList<>();
        Map<Object, Object> hash = stringRedisTemplate.opsForHash().entries(key);
        for (Object field : hash.keySet()) {
            if (((String) field).startsWith("child:")) {
                children.add((String) hash.get(field));
            }
        }
        return children;
    }

    // 在文本中查找敏感词组（只匹配完整的敏感词组）
    public Set<String> searchAllPatternGroups(String text) {
        Set<String> foundPatterns = new HashSet<>();
        String currentNodeId = ROOT_NODE_ID;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            // 找到可以匹配的节点，或退回到根节点
            while (currentNodeId != null && !stringRedisTemplate.opsForHash().hasKey("trie:" + currentNodeId, "child:" + ch)) {
                currentNodeId = (String) stringRedisTemplate.opsForHash().get("trie:" + currentNodeId, "failureLink");
            }

            if (currentNodeId == null) {
                currentNodeId = ROOT_NODE_ID;
                continue;
            }

            currentNodeId = (String) stringRedisTemplate.opsForHash().get("trie:" + currentNodeId, "child:" + ch);

            // 如果有输出，表示匹配到了敏感词
            String output = (String) stringRedisTemplate.opsForHash().get("trie:" + currentNodeId, "output");
            if (output != null) {
                foundPatterns.add(output);
            }
        }

        // 获取所有敏感词组
        Set<String> matchedPatterns = new HashSet<>();
        Map<Object, Object> groups = stringRedisTemplate.opsForHash().entries("sensitive:groups");
        for (Map.Entry<Object, Object> entry : groups.entrySet()) {
            String[] groupPatterns = ((String) entry.getValue()).split(",");
            if (foundPatterns.containsAll(Arrays.asList(groupPatterns))) {
                matchedPatterns.addAll(Arrays.asList(groupPatterns)); // 添加匹配到的组内所有敏感词
            }
        }

        return matchedPatterns;
    }

    public static void main(String[] args) {
        // 创建 StringRedisTemplate 实例（实际使用时可通过依赖注入）
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

        AhoCorasickRedisUtil acRedis = new AhoCorasickRedisUtil(stringRedisTemplate);

        // 添加敏感词
        acRedis.addPattern("敏感词1");
        acRedis.addPattern("敏感词2");
        acRedis.addPattern("测试");
        acRedis.addPattern("王经鹏");
        acRedis.addPattern("赣榆区人大区长");

        // 添加敏感词组
        List<String> groupPatterns = Arrays.asList("敏感词1", "测试");
        acRedis.addPatternGroup("group1", groupPatterns);
        List<String> groupPatterns2 = Arrays.asList("敏感词1", "敏感词4");
        acRedis.addPatternGroup("group2", groupPatterns2);

        // 构建失败指针
        acRedis.buildFailureLinks();

        // 在文本中查找敏感词组
        String text = "这王经鹏是一个包含敏感词4和测试的文赣榆区人敏感词1大区长本。";
        Set<String> matchedGroups = acRedis.searchAllPatternGroups(text);
        if (!matchedGroups.isEmpty()) {
            System.out.println("文本中包含以下敏感词组：" + matchedGroups);
        } else {
            System.out.println("文本中不包含完整的敏感词组。");
        }
    }
}
