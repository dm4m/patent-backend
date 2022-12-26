package cn.edu.bit.patentbackend.repository;

import cn.edu.bit.patentbackend.bean.SearchResponse;
import cn.edu.bit.patentbackend.mapper.PatentMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class PatentRepository {

    @Autowired
    PatentMapper patentMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public SearchHits basicSearch(SearchRequest request) throws IOException {
        org.elasticsearch.action.search.SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        // 查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        return hits;
    }

    public List<Map<String, Object>> getPatentById(List<Integer> idList) {
//        List<Map<String, Object>>
//        PatentWithBLOBs patent = patentMapper.selectByPrimaryKey(list.get(0));
        if(idList.isEmpty()){
            return Arrays.asList();
        }
        List<Map<String, Object>> patents = patentMapper.selectPatentListByIds(idList);
        return patents;
    }

    public SearchResponse searchByQueryBuilder(QueryBuilder queryBuilder, int curPage, int perPage) {
        curPage = curPage >= 0 ? curPage : 0;
        perPage = perPage > 0 ? perPage : 20;
        int from = curPage * perPage;
        int size = (perPage);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).from(from).size(size);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        SearchHits hits = null;
        try {
            org.elasticsearch.action.search.SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            hits = response.getHits();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        ArrayList<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //生成序号
            sourceAsMap.put("index", curPage * perPage + (++i));
            results.add(sourceAsMap);
//            System.out.println(hit.getSourceAsString());
        }
//        System.out.println("<<========");
//        System.out.println("收到请求");
        long totalHits = hits.getTotalHits().value;
        int pageNum = (int)totalHits / perPage;
        SearchResponse response = new SearchResponse(curPage, totalHits, pageNum, perPage, "", "","", null, results);
        return response;
    }

//    public SearchHits advancedSearch(SearchRequest request) {
//    }
}
