package cn.wenxun.spider;

import redis.clients.jedis.Jedis;

import java.util.*;

public class AhoCorasickRedisUtil {
    private static class TrieNode {
        Map<Character, String> children = new HashMap<>(); // 子节点，保存子节点的ID
        String failureLink = null; // 失败指针的ID
        List<String> output = new ArrayList<>(); // 记录匹配到的模式串
    }

    private final Jedis jedis;
    private final String ROOT_NODE_ID = "root";

    public AhoCorasickRedisUtil(Jedis jedis) {
        this.jedis = jedis;
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
            jedis.hmset(key, hash);
        }
    }

    // 添加敏感词到 Trie 树并存储到 Redis 中
    public void addPattern(String pattern) {
        String currentNodeId = ROOT_NODE_ID;

        for (char ch : pattern.toCharArray()) {
            String key = "trie:" + currentNodeId;
            String childNodeId = jedis.hget(key, "child:" + ch);

            if (childNodeId == null) {
                // 创建新的节点
                childNodeId = UUID.randomUUID().toString();
                TrieNode newNode = new TrieNode();
                storeNodeToRedis(childNodeId, newNode);

                // 更新当前节点的子节点信息到 Redis
                jedis.hset(key, "child:" + ch, childNodeId);
            }
            currentNodeId = childNodeId;
        }

        // 标记该节点为敏感词结束
        String finalKey = "trie:" + currentNodeId;
        jedis.hset(finalKey, "output", pattern);
    }

    // 添加敏感词组到 Redis 中
    public void addPatternGroup(String groupName, List<String> patterns) {
        String groupKey = "sensitive:groups";
        jedis.hset(groupKey, groupName, String.join(",", patterns));
    }

    // 构建失败指针并存储到 Redis 中
    public void buildFailureLinks() {
        Queue<String> queue = new LinkedList<>();
        // 初始化根节点子节点的失败指针为根节点
        for (String childId : getChildren(ROOT_NODE_ID)) {
            jedis.hset("trie:" + childId, "failureLink", ROOT_NODE_ID);
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
                String failNodeId = jedis.hget("trie:" + currentNodeId, "failureLink");
                while (failNodeId != null && !jedis.hexists("trie:" + failNodeId, "child:" + ch)) {
                    failNodeId = jedis.hget("trie:" + failNodeId, "failureLink");
                }

                if (failNodeId != null) {
                    jedis.hset("trie:" + childNodeId, "failureLink", jedis.hget("trie:" + failNodeId, "child:" + ch));
                } else {
                    jedis.hset("trie:" + childNodeId, "failureLink", ROOT_NODE_ID);
                }

                // 继承失败指针的输出
                String failureLinkNodeId = jedis.hget("trie:" + childNodeId, "failureLink");
                if (failureLinkNodeId != null) {
                    String failureOutput = jedis.hget("trie:" + failureLinkNodeId, "output");
                    if (failureOutput != null) {
                        jedis.hset("trie:" + childNodeId, "output", failureOutput);
                    }
                }

                queue.add(childNodeId);
            }
        }
    }

    // 从 Redis 中加载节点信息
    private TrieNode loadNodeFromRedis(String nodeId) {
        String key = "trie:" + nodeId;
        Map<String, String> hash = jedis.hgetAll(key);

        TrieNode node = new TrieNode();
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

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
        Map<String, String> hash = jedis.hgetAll(key);
        for (String field : hash.keySet()) {
            if (field.startsWith("child:")) {
                children.add(hash.get(field));
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
            while (currentNodeId != null && !jedis.hexists("trie:" + currentNodeId, "child:" + ch)) {
                currentNodeId = jedis.hget("trie:" + currentNodeId, "failureLink");
            }

            if (currentNodeId == null) {
                currentNodeId = ROOT_NODE_ID;
                continue;
            }

            currentNodeId = jedis.hget("trie:" + currentNodeId, "child:" + ch);

            // 如果有输出，表示匹配到了敏感词
            String output = jedis.hget("trie:" + currentNodeId, "output");
            if (output != null) {
                foundPatterns.add(output);
            }
        }

        // 获取所有敏感词组
        Set<String> matchedPatterns = new HashSet<>();
        Map<String, String> groups = jedis.hgetAll("sensitive:groups");
        for (Map.Entry<String, String> entry : groups.entrySet()) {
            String[] groupPatterns = entry.getValue().split(",");
            if (foundPatterns.containsAll(Arrays.asList(groupPatterns))) {
                matchedPatterns.addAll(Arrays.asList(groupPatterns)); // 添加匹配到的组内所有敏感词
            }
        }

        return matchedPatterns;
    }

    public static void main(String[] args) {
        // 创建 Jedis 实例（实际使用时可通过依赖注入）
        long startTime = System.currentTimeMillis();
        Jedis jedis = new Jedis("123.56.27.46", 6379);
        jedis.auth("wangjingpeng2659");

        AhoCorasickRedisUtil acRedis = new AhoCorasickRedisUtil(jedis);
        System.out.println("redis连接耗时：" + (System.currentTimeMillis() - startTime));
        // 添加敏感词
        acRedis.addPattern("敏感词1");
        acRedis.addPattern("敏感词2");
        acRedis.addPattern("敏感词4");
        acRedis.addPattern("敏感词3");
        acRedis.addPattern("赣榆区人大区长");
        System.out.println("敏感词添加耗时：" + (System.currentTimeMillis() - startTime));
        // 添加敏感词组
        List<String> groupPatterns = Arrays.asList("敏感词1", "测试");
        acRedis.addPatternGroup("group1", groupPatterns);
        List<String> groupPatterns2 = Arrays.asList("敏感词4", "敏感词3");
        acRedis.addPatternGroup("group2", groupPatterns2);
        System.out.println("敏感词组添加耗时：" + (System.currentTimeMillis() - startTime));
        // 构建失败指针
        acRedis.buildFailureLinks();

        // 在文本中查找敏感词组
        String text = "这王经鹏是一敏感词4个包含敏感词1和测试敏感词3的文赣榆区人大区长本。";
         Set<String> matchedGroups = acRedis.searchAllPatternGroups(text);
        System.out.println("文本耗时：" + (System.currentTimeMillis() - startTime));
        if (!matchedGroups.isEmpty()) {
            System.out.println("文本中包含以下敏感词组：" + matchedGroups);
        } else {
            System.out.println("文本中不包含完整的敏感词组。");
        }
    }
}
