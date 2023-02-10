package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;

import java.util.ArrayList;
import java.util.List;

public interface StatsAnaService {
    ArrayList<AnalysisCollection> getAnalysisCollection();

    AnalysisCollectionItemRsp getAnalysisCollectionItems(Integer collectionId, Integer pageIndex, Integer pageSize);

    void deleteAnalysisCollectionItems(List<Integer> itemIds);

    void deleteAnalysisCollection(Integer collectionId);

    void insertAnalysisCollectionItems(List patentIds, Integer collectionId);

    void insertAnalysisCollection(String collectionName);
}
