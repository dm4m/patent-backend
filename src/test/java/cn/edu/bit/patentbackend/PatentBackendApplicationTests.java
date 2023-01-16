package cn.edu.bit.patentbackend;

import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItem;
import cn.edu.bit.patentbackend.mapper.PatentMapper;
import cn.edu.bit.patentbackend.utils.ExpressionUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class PatentBackendApplicationTests {

    @Value("${flask.url}")
    String flaskUrl;
    @Autowired
    PatentMapper patentMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() throws JsonProcessingException {
        WebClient webClient = WebClient.create(flaskUrl);
        Mono<String> mono = webClient.get()
                .uri("/?query={query}", "一种烟花制品")
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve().bodyToMono(String.class);
        String httpResponse = mono.block();
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> alist = mapper.readValue(httpResponse, ArrayList.class);
        System.out.println("--- end ---");
//        HttpStatus statusCode = response.statusCode(); // 获取响应码
//        int statusCodeValue = response.rawStatusCode(); // 获取响应码值
//        ClientResponse.Headers headers = response.headers(); // 获取响应头
//
//        // 获取响应体
//        Mono<String> resultMono = response.bodyToMono(String.class);
//        String body = resultMono.block();
//
//        // 输出结果
//        System.out.println("statusCode：" + statusCode);
//        System.out.println("statusCodeValue：" + statusCodeValue);
//        System.out.println("headers：" + headers.asHttpHeaders());
//        System.out.println("body：" + body);
//        System.out.println("ddd");
    }

    @Test
    void testESJavaApi() throws IOException {
        // Create the new Java Client with the same low level client
        ElasticsearchTransport transport = new RestClientTransport(
                restHighLevelClient.getLowLevelClient(),
                new JacksonJsonpMapper()
        );
        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);
        SearchResponse<Object> search = client.search(s -> s
                        .index("patent")
                        .query(q -> q
                                .term(t -> t
                                        .field("agency")
                                        .value(v -> v.stringValue("湖南省专利服务中心"))))
                , Object.class);

        System.out.println(" ");
    }

    @Test
    void testSignoryNeuralSearch(){

    }

    @Test
    void boolQueryTest() throws IOException {
        // title=烟花 and abstract=纸筒
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchQuery("title", "烟花"));
        boolQueryBuilder.must(QueryBuilders.matchQuery("abstract", "纸筒"));
        BoolQueryBuilder fb = new BoolQueryBuilder();
        fb.must(boolQueryBuilder);
        fb.must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("inventor_list", "李欣")).should(QueryBuilders.matchQuery("inventor_list", "吴俊和")));
        searchSourceBuilder.query(fb);
        searchRequest.source(searchSourceBuilder);
        org.elasticsearch.action.search.SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
    }

    @Test
    void expressionParser() throws IOException {
        String expression = "(abstract=\"纸筒\" ! title=烟花)";
        QueryBuilder builder = ExpressionUtil.expression2Query(expression);
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(builder);
        searchRequest.source(searchSourceBuilder);
        org.elasticsearch.action.search.SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
    }

    @Test
    void preprocessTest() {
        String a = "（title=  “123”）";
        String preprocess = ExpressionUtil.preprocess(a);
        System.out.println(preprocess);
    }
    @Test
    void testSql(){
//        List<AnalysisCollection> allAnalysisCollection = patentMapper.getAllAnalysisCollection();
//        System.out.println(allAnalysisCollection);
//        List<AnalysisCollectionItem> analysisCollectionItems = patentMapper.getAnalysisCollectionItems(1, 3, 0);
//        System.out.println(analysisCollectionItems);
//        Integer acItemsAccount = patentMapper.getACItemsAccount(1);
//        System.out.println(acItemsAccount);
//        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(4, 5));
//        patentMapper.deleteAnalysisCollectionItems(list);
        patentMapper.deleteAnalysisCollectionById(3);
    }
}
