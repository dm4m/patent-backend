package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.basicSearchResponse;
import cn.edu.bit.patentbackend.bean.mySQLSearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;


public interface PatentService {
    public basicSearchResponse basicSearch(String query, String field, Integer page, Integer perPage) throws IOException;

    basicSearchResponse neuralSearch(String query, Integer curPage, Integer perPage) throws JsonProcessingException;
}
