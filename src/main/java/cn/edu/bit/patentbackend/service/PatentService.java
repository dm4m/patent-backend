package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.SearchResponse;

import java.io.IOException;


public interface PatentService {
    public SearchResponse search(String query, String field, Integer page, Integer perPage) throws IOException;
}
