package cn.edu.bit.patentbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PatentBackendApplicationTests {

    @Value("${flask.url}")
    String flaskUrl;

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

}
