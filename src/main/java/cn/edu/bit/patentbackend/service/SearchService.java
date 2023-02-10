package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.SearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;


public interface SearchService {
    public SearchResponse basicSearch(String query, String field, Integer page, Integer perPage) throws IOException;

    public SearchResponse neuralSearch(String query, String field, Integer curPage, Integer perPage) throws JsonProcessingException;

    SearchResponse proSearch(String expression, Integer curPage, Integer perPage);

    SearchResponse advancedSearch(LinkedHashMap conditions, int curPage, int perPage);

    SearchResponse uploadSearch(MultipartFile file) throws JsonProcessingException;


}
