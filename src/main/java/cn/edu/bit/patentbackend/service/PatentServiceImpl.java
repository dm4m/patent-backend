package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.BasicSearchResponse;
import cn.edu.bit.patentbackend.repository.PatentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PatentServiceImpl implements PatentService{

    @Autowired
    PatentRepository patentRepository;

    @Value("${flask.url}")
    String flaskUrl;

    @Override
    public BasicSearchResponse basicSearch(String query, String field, Integer curPage, Integer perPage) throws IOException {
        curPage = curPage >= 0 ? curPage : 0;
        perPage = perPage > 0 ? perPage : 20;
        int from = curPage * perPage;
        int size = (perPage);
        ArrayList<Map<String, Object>> results = new ArrayList<>();
        // 创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("patent");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if(field.equals("title")){
            sourceBuilder.query(QueryBuilders.matchQuery("title", query));
        }else if(field.equals("abstract")){
            sourceBuilder.query(QueryBuilders.matchQuery("abstract", query));
        }else if(field.equals("signory_item")){
            sourceBuilder.query(QueryBuilders.matchQuery("signory_item", query));
        }else if(field.equals("patentCode")){
            sourceBuilder.query(QueryBuilders.termQuery("patent_code.keyword", query));
        }else if(field.equals("publicationNo")){
            sourceBuilder.query(QueryBuilders.termQuery("publication_no.keyword", query));
        }else if(field.equals("applicant")){
            sourceBuilder.query(QueryBuilders.wildcardQuery("applicant_list.keyword", "*" + query + "*"));
        }else if(field.equals("inventor")){
            sourceBuilder.query(QueryBuilders.wildcardQuery("inventor_list.keyword", "*" + query + "*"));
        }else if(field.equals("main_class")){
            sourceBuilder.query(QueryBuilders.wildcardQuery("main_class_list.keyword", "*" + query + "*"));
        }else if(field.equals("class")){
            sourceBuilder.query(QueryBuilders.wildcardQuery("class_list.keyword", "*" + query + "*"));
        }
//        sourceBuilder.query(QueryBuilders.multiMatchQuery(query, field));
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        request.source(sourceBuilder);
        SearchHits hits = null;
        try {
            hits = patentRepository.basicSearch(request);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        int i = 0;
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //生成序号
            sourceAsMap.put("index", curPage * perPage + (++i));
            results.add(sourceAsMap);
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
        System.out.println("收到请求");
        long totalHits = hits.getTotalHits().value;
        int pageNum = (int)totalHits / perPage;
        BasicSearchResponse response = new BasicSearchResponse(curPage, totalHits, pageNum, perPage, query, field,"basic", results);
        return response;
    }

    @Override
    public BasicSearchResponse neuralSearch(String query, String field, Integer curPage, Integer perPage) throws JsonProcessingException {
        ArrayList<Map<String, Object>> results = new ArrayList<>();
        //通过query获取id list
        WebClient webClient = WebClient.create(flaskUrl);
        Mono<String> mono = webClient.get()
                .uri("/?query={query}&field={field}", query, field)
                .retrieve()
                .bodyToMono(String.class);
        String httpResponse = mono.block();
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> list = mapper.readValue(httpResponse, ArrayList.class);
//        List<Integer> list = Arrays.asList(1, 2);
        if(field.equals("signory_item")){

        }
        List<Map<String, Object>> patentList = patentRepository.getPatentById(list);
        int i = 0;
        for (Map patent : patentList) {
            //生成序号
            patent.put("index", curPage * perPage + (++i));
            results.add(patent);
        }
        int totalHits = patentList.size();
        int pageNum = totalHits / perPage;
        BasicSearchResponse response = new BasicSearchResponse(curPage, (long)totalHits, pageNum, perPage, query, "title", "neural",results);
        return response;
    }
}
