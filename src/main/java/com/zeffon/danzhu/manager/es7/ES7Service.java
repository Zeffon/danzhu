package com.zeffon.danzhu.manager.es7;

import com.alibaba.fastjson.JSON;
import com.zeffon.danzhu.manager.es7.document.FileDocument;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Zeffon on 2021/3/21
 */
@Service
public class ES7Service {

    @Autowired
    private RestHighLevelClient esClient;

    public List<FileDocument> getDoc(String title) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Constant.INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", title);
        searchSourceBuilder.query(termQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getSearchResult(searchResponse);
    }

    public void addDoc(FileDocument document) {
        IndexRequest indexRequest = new IndexRequest(Constant.INDEX)
                .id(String.valueOf(document.getId()))
                .source(JSON.toJSONString(document), XContentType.JSON);
        try {
            esClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteDoc(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(Constant.INDEX, id);
        try {
            esClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createIndex(String index) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );
        createIndexRequest.mapping("{\n" +
                "  \"properties\": {\n" +
                "    \"id\": {\n" +
                "      \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"userId\": {\n" +
                "      \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"size\": {\n" +
                "      \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"createTime\": {\n" +
                "      \"type\": \"text\"\n" +
                "    },\n" +
                "    \"url\": {\n" +
                "      \"type\": \"keyword\",\n" +
                "      \"index\": false\n" +
                "    },\n" +
                "    \"title\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"fields\": {\n" +
                "          \"keyword\": {\n" +
                "             \"ignore_above\": 256,\n" +
                "             \"type\": \"keyword\"\n" +
                "          }\n" +
                "       }\n" +
                "    }\n" +
                "  }\n" +
                "}", XContentType.JSON);
        CreateIndexResponse createIndexResponse = esClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    private List<FileDocument> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<FileDocument> fileDocuments = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            fileDocuments.add(JSON.parseObject(hit.getSourceAsString(), FileDocument.class));
        }
        return fileDocuments;
    }
}
