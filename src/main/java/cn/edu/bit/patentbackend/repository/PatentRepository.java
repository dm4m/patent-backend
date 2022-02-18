package cn.edu.bit.patentbackend.repository;

import cn.edu.bit.patentbackend.bean.PatentWithBLOBs;
import cn.edu.bit.patentbackend.mapper.PatentMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class PatentRepository {

    @Autowired
    PatentMapper patentMapper;

    @Value("${elasticsearch.ip}")
    private String es_ip;

    @Value("${elasticsearch.port}")
    private Integer es_port;

    public SearchHits basicSearch(SearchRequest request) throws IOException {
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

    public List<Map<String, Object>> getPatentById(List<Integer> idList) {
//        List<Map<String, Object>>
//        PatentWithBLOBs patent = patentMapper.selectByPrimaryKey(list.get(0));
        if(idList.isEmpty()){
            return Arrays.asList();
        }
        List<Map<String, Object>> patents = patentMapper.selectPatentListByIds(idList);
        return patents;
    }

}
