package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.AdvancedSearchCondition;
import cn.edu.bit.patentbackend.bean.BasicSearchResponse;
import cn.edu.bit.patentbackend.repository.PatentRepository;
import cn.edu.bit.patentbackend.utils.ExpressionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilder;
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
import java.util.*;

@Service
public class PatentServiceImpl implements PatentService{

    @Autowired
    PatentRepository patentRepository;

    @Value("${flask.url}")
    String flaskUrl;

    @Override
    public BasicSearchResponse basicSearch(String query, String field, Integer curPage, Integer perPage) throws IOException {
        QueryBuilder queryBuilder = null;
        if(field.equals("title")){
            queryBuilder = QueryBuilders.matchQuery("title", query);
        }else if(field.equals("abstract")){
            queryBuilder = QueryBuilders.matchQuery("abstract", query);
        }else if(field.equals("signoryItem")){
            queryBuilder = QueryBuilders.matchQuery("signory_item", query);
        }else if(field.equals("patentCode")){
            queryBuilder = QueryBuilders.termQuery("patent_code.keyword", query);
        }else if(field.equals("publicationNo")){
            queryBuilder = QueryBuilders.termQuery("publication_no.keyword", query);
        }else if(field.equals("applicant")){
            queryBuilder = QueryBuilders.wildcardQuery("applicant_list.keyword", "*" + query + "*");
        }else if(field.equals("inventor")){
            queryBuilder = QueryBuilders.wildcardQuery("inventor_list.keyword", "*" + query + "*");
        }else if(field.equals("mainClass")){
            queryBuilder = QueryBuilders.wildcardQuery("main_class_list.keyword", "*" + query + "*");
        }else if(field.equals("class")){
            queryBuilder = QueryBuilders.wildcardQuery("class_list.keyword", "*" + query + "*");
        }
        BasicSearchResponse response = patentRepository.searchByQueryBuilder(queryBuilder, curPage, perPage);
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

    @Override
    public BasicSearchResponse proSearch(String expression, Integer curPage, Integer perPage) {
        QueryBuilder queryBuilder = ExpressionUtil.expression2Query(expression);
        BasicSearchResponse response = patentRepository.searchByQueryBuilder(queryBuilder, curPage, perPage);
        return response;
    }

    @Override
    public BasicSearchResponse advancedSearch(LinkedHashMap conditionMap, int curPage, int perPage) {
        ArrayList<AdvancedSearchCondition> conditions = new ArrayList<>(conditionMap.values());
        QueryBuilder queryBuilder = ExpressionUtil.condition2Query(conditions);
        BasicSearchResponse response = patentRepository.searchByQueryBuilder(queryBuilder, curPage, perPage);
        return response;
    }


}
