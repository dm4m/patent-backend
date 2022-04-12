package cn.edu.bit.patentbackend;

import cn.edu.bit.patentbackend.bean.BasicSearchResponse;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PatentBackendApplicationTests {

    @Value("${flask.url}")
    String flaskUrl;

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

}
