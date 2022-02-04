package cn.edu.bit.patentbackend.repository;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class PatentRepository {

    @Value("${elasticsearch.ip}")
    private String es_ip;

    @Value("${elasticsearch.port}")
    private Integer es_port;

    public SearchHits search(SearchRequest request) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(es_ip, es_port, "http")));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        return hits;
    }
}
