package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;
import cn.edu.bit.patentbackend.bean.stats.NoveltyAnaResult;
import cn.edu.bit.patentbackend.bean.stats.NoveltyAnaResultItemRsp;

import java.util.ArrayList;
import java.util.List;

public interface StatsAnaService {
    ArrayList<AnalysisCollection> getAnalysisCollection();

    AnalysisCollectionItemRsp getAnalysisCollectionItems(Integer collectionId, Integer pageIndex, Integer pageSize);

    void deleteAnalysisCollectionItems(List<Integer> itemIds);

    void deleteAnalysisCollection(Integer collectionId);

    void insertAnalysisCollectionItems(List patentIds, Integer collectionId);

    Integer insertAnalysisCollection(String collectionName);

    ArrayList<NoveltyAnaResult> getNoveltyAnaResults();

    NoveltyAnaResultItemRsp getNoveltyAnaResultItems(Integer noveltyAnaId, Integer pageIndex, Integer pageSize);

    void deleteNoveltyRes(Integer noveltyResId);
}
