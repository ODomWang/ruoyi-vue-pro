package cn.wenxun.admin.core.service;

import cn.iocoder.yudao.module.system.model.MeiliSearchInfo;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.*;

import java.util.List;

/**
 * @author wangy
 */
public interface MeiliSearchOperations<T> {

    Stats getStats() throws MeilisearchException;

    void clearIndex(String uid) throws MeilisearchException;

    TaskInfo createIndex(String uid, String primaryKey) throws MeilisearchException;

    T get(String id) throws MeilisearchException;

    Results<T> list() throws MeilisearchException;

    Results<T> list(int offset, int limit) throws MeilisearchException;

    TaskInfo add(T document);

    long update(T document);

    TaskInfo add(List<T> documents) throws MeilisearchException;

    long update(List<T> documents);

    void delete(String identifier) throws MeilisearchException;

    long deleteBatch(String... documentsIdentifiers);

    long deleteAll();

    Task getTask(int taskUid);

    Results<MultiSearchResult> search(MeiliSearchInfo meiliSearchInfo);

    SearchResultPaginated searchPage(MeiliSearchInfo size);


    SearchResult search(String q, String classify, int offset, int limit);

    SearchResult search(String q, int offset, int limit, String[] attributesToRetrieve, String[] attirbutesToCrop, Integer cropLength, String cropMarker, String[] filter, String[] sort);

    Searchable search(SearchRequest sr);

    SearchResultPaginated searchPaginated(SearchRequest sr);
}