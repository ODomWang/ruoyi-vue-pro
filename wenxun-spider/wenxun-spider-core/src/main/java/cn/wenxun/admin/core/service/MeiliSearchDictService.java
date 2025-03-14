package cn.wenxun.admin.core.service;

import cn.iocoder.yudao.module.system.dal.dataobject.wenxunDict.WenXunDictDataDO;
import cn.iocoder.yudao.module.system.model.MeiliSearchInfo;
import com.alibaba.fastjson.JSON;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class MeiliSearchDictService implements InitializingBean, MeiliSearchOperations<WenXunDictDataDO> {


    private Index index;

    @Resource
    private Client client;

    @Value("${meilisearch.uid}")
    private String uid;

    @Override
    public Stats getStats() throws MeilisearchException {

        return client.getStats();
    }

    @Override
    public void clearIndex(String uid) throws MeilisearchException {
        client.index(uid).deleteAllDocuments();
    }

    /**
     * @param uid
     * @throws MeilisearchException
     */
    @Override
    public TaskInfo createIndex(String uid, String primaryKey) throws MeilisearchException {
        return client.createIndex(uid, primaryKey);
    }

    @Override
    public WenXunDictDataDO get(String id) throws MeilisearchException {
        return index.getDocument(id, WenXunDictDataDO.class);
    }

    @Override
    public Results<WenXunDictDataDO> list() throws MeilisearchException {
        DocumentsQuery query = new DocumentsQuery();
        return index.getDocuments(query, WenXunDictDataDO.class);
    }

    @Override
    public Results<WenXunDictDataDO> list(int offset, int limit) throws MeilisearchException {
        DocumentsQuery query = new DocumentsQuery().setLimit(limit).setOffset(offset);
        return index.getDocuments(query, WenXunDictDataDO.class);
    }

    @Override
    public TaskInfo add(WenXunDictDataDO document) {
        return index.addDocuments(JSON.toJSONString(document));

    }

    @Override
    public long update(WenXunDictDataDO document) {
        return 0;
    }

    @Override
    public TaskInfo add(List<WenXunDictDataDO> documents) throws MeilisearchException {
        return index.updateDocuments(JSON.toJSONString(documents));
    }

    @Override
    public long update(List<WenXunDictDataDO> documents) {
        return 0;
    }

    @Override
    public void delete(String identifier) throws MeilisearchException {
        index.deleteDocument(identifier);
    }

    @Override
    public long deleteBatch(String... documentsIdentifiers) {
        return 0;
    }

    @Override
    public long deleteAll() {
        return 0;
    }

    @Override
    public Task getTask(int taskUid) {
        return client.getTask(taskUid);

    }


    /**
     * 搜索参数：https://www.meilisearch.com/docs/reference/api/search#customize-attributes-to-search-on-at-search-time
     *      q：查询字符串
     *      attributesToSearchOn：将搜索限制为指定属性
     *      offset：要跳过的文档数
     *      limit：返回的最大文档数
     */

    /**
     * 仅在标题下查询
     *

     */
    @Override
    public Results<MultiSearchResult> search(MeiliSearchInfo meiliSearchInfo) {
        return null;
    }

    @Override
    public SearchResultPaginated searchPage(MeiliSearchInfo meiliSearchInfo) {
        return null;
    }

    /**
     * 在标题和简介中查询，并按照appraise排序
     *
     * @param q        关键字
     * @param classify 书籍类型
     * @param offset
     * @param limit
     */
    @Override
    public SearchResult search(String q, String classify, int offset, int limit) {
        SearchRequest searchRequest = SearchRequest.builder().q(q).attributesToSearchOn(new String[]{"title", "synopsis"})
                .attributesToHighlight(new String[]{"title", "synopsis"})
                .highlightPreTag("<span class=\"highlight\">")
                .highlightPostTag("</span>")
                .sort(new String[]{"appraise:desc"})
                .offset(offset)
                .limit(limit)
                .build();
        if (!"all".equalsIgnoreCase(classify)) {
            searchRequest.setFilterArray(new String[][]{
                    new String[]{"classify = " + classify}
            });
        }
        return search(searchRequest);
    }

    /**
     * 查询，因为meilisearch在高亮功能上对汉字处理较弱，所以不处理高亮
     *
     * @param q                    关键字,查询字符串,Meilisearch只考虑任何给定搜索查询的前十个单词。
     * @param offset               跳过文档数
     * @param limit                结果返回限值条数
     * @param attributesToRetrieve 结果返回属性 null或数组长度为0时返回全部数据
     * @param attirbutesToCrop     必须裁剪其值的属性
     * @param cropLength           裁剪值的最大长度（以字为单位）
     * @param cropMarker           字符串标记裁剪边界
     * @param filter               只查询filter中属性
     * @param sort                 按哪个属性排序
     * @return
     */
    @Override
    public SearchResult search(String q, int offset, int limit, String[] attributesToRetrieve, String[] attirbutesToCrop, Integer cropLength, String cropMarker, String[] filter, String[] sort) {
        return null;
    }

    /**
     * 封装底层搜索
     *
     * @param sr 搜索条件
     */
    @Override
    public SearchResult search(SearchRequest sr) {
        try {
            return (SearchResult) index.search(sr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 封装底层搜索
     *
     * @param sr 搜索条件
     */
    @Override
    public SearchResultPaginated searchPaginated(SearchRequest sr) {
        try {
            return (SearchResultPaginated) index.search(sr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化索引信息
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        index = client.getIndex("wenxun_dict_data");
        Settings settings = new Settings();

        settings.setFilterableAttributes(new String[]{"label", "value", "remark"});
        settings.setSearchableAttributes(new String[]{"label", "value", "remark"});
         index.updateSettings(settings);

        System.out.println("初始化完成MeiliSearch的索引信息:wenxun_dict_data");
    }
}
