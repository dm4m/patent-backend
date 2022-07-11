package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.BasicSearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.LinkedHashMap;


public interface PatentService {
    public BasicSearchResponse basicSearch(String query, String field, Integer page, Integer perPage) throws IOException;

    public BasicSearchResponse neuralSearch(String query, String field, Integer curPage, Integer perPage) throws JsonProcessingException;

    BasicSearchResponse proSearch(String expression);

//    BasicSearchResponse advancedSearch(LinkedHashMap conditions, int curPage, int perPage);
}
