package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.BasicSearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;


public interface PatentService {
    public BasicSearchResponse basicSearch(String query, String field, Integer page, Integer perPage) throws IOException;

    BasicSearchResponse neuralSearch(String query, String field, Integer curPage, Integer perPage) throws JsonProcessingException;
}
