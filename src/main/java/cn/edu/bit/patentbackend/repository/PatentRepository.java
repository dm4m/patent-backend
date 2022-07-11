package cn.edu.bit.patentbackend.repository;

import cn.edu.bit.patentbackend.bean.PatentWithBLOBs;
import cn.edu.bit.patentbackend.mapper.PatentMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class PatentRepository {

    @Autowired
    PatentMapper patentMapper;

//    @Value("${elasticsearch.ip}")
//    private String es_ip;
//
//    @Value("${elasticsearch.port}")
//    private Integer es_port;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public SearchHits basicSearch(SearchRequest request) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
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

//    public SearchHits advancedSearch(SearchRequest request) {
//    }
}
