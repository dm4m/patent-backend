package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.repository.PatentRepository;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class PatentServiceImpl implements PatentService{

    @Autowired
    PatentRepository patentRepository;

    @Override
    public ArrayList<Map<String, Object>> search(String query) throws IOException {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        // 创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("patent");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("abstract", query));
        request.source(sourceBuilder);
        SearchHits hits = patentRepository.search(request);
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            result.add(sourceAsMap);
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
        System.out.println("收到请求");
        return result;
    }
}
