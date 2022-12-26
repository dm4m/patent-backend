package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.AdvancedSearchCondition;
import cn.edu.bit.patentbackend.bean.SearchResponse;
import cn.edu.bit.patentbackend.repository.PatentRepository;
import cn.edu.bit.patentbackend.utils.ExpressionUtil;
import cn.edu.bit.patentbackend.utils.PatentMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

@Service
public class PatentServiceImpl implements PatentService{

    @Autowired
    PatentRepository patentRepository;

    @Value("${flask.url}")
    String flaskUrl;

    @Override
    public SearchResponse basicSearch(String query, String field, Integer curPage, Integer perPage) throws IOException {
        QueryBuilder queryBuilder = null;
        queryBuilder = PatentMapper.simpleQuery(field, query);
        System.out.println(queryBuilder);
        SearchResponse response = patentRepository.searchByQueryBuilder(queryBuilder, curPage, perPage);
        response.setSearchType("basic");
        response.setField(field);
        response.setQuery(query);
        return response;
    }

    @Override
    public SearchResponse neuralSearch(String query, String field, Integer curPage, Integer perPage) throws JsonProcessingException {
        ArrayList<Map<String, Object>> results = new ArrayList<>();
        //通过query获取id list
        WebClient webClient = WebClient.create(flaskUrl);
        Mono<String> mono = webClient.get()
                .uri("/neuralSearch/?query={query}&field={field}", query, field)
                .retrieve()
                .bodyToMono(String.class);
        String httpResponse = mono.block();
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> list = mapper.readValue(httpResponse, ArrayList.class);
//        List<Integer> list = Arrays.asList(1, 2);
        List<Map<String, Object>> patentList = patentRepository.getPatentById(list);
        int i = 0;
        for (Map patent : patentList) {
            //生成序号
            patent.put("index", curPage * perPage + (++i));
            results.add(patent);
        }
        int totalHits = patentList.size();
        int pageNum = totalHits / perPage;
        SearchResponse response = new SearchResponse(curPage, (long)totalHits, pageNum, perPage, query, "title", "neural", null, results);
        return response;
    }

    @Override
    public SearchResponse proSearch(String expression, Integer curPage, Integer perPage) {
        QueryBuilder queryBuilder = ExpressionUtil.expression2Query(expression);
        SearchResponse response = patentRepository.searchByQueryBuilder(queryBuilder, curPage, perPage);
        response.setSearchType("pro");
        response.setQuery(expression);
        return response;
    }

    @Override
    public SearchResponse advancedSearch(LinkedHashMap conditionMap, int curPage, int perPage) {
        ArrayList<AdvancedSearchCondition> conditions = new ArrayList<>(conditionMap.values());
        QueryBuilder queryBuilder = ExpressionUtil.condition2Query(conditions);
        SearchResponse response = patentRepository.searchByQueryBuilder(queryBuilder, curPage, perPage);
        response.setSearchType("advanced");
        response.setConditionMap(conditionMap);
        return response;
    }

}
