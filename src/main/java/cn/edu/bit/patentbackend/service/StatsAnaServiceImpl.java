package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItem;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;
import cn.edu.bit.patentbackend.mapper.StatsAnaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class StatsAnaServiceImpl implements StatsAnaService{

    @Autowired
    StatsAnaMapper statsAnaMapper;

    @Override
    public ArrayList<AnalysisCollection> getAnalysisCollection() {
        return (ArrayList<AnalysisCollection>) statsAnaMapper.getAllAnalysisCollection();
    }

    @Override
    public AnalysisCollectionItemRsp getAnalysisCollectionItems(Integer collectionId, Integer pageIndex, Integer pageSize) {
        List<AnalysisCollectionItem> analysisCollectionItems = statsAnaMapper.getAnalysisCollectionItems(collectionId, pageSize, pageIndex * pageSize);
        Integer acItemsAccount = statsAnaMapper.getACItemsAccount(collectionId);
        AnalysisCollectionItemRsp analysisCollectionItemRsp = new AnalysisCollectionItemRsp(analysisCollectionItems, acItemsAccount);
        return analysisCollectionItemRsp;
    }

    @Override
    public void deleteAnalysisCollectionItems(List<Integer> itemIds) {
        statsAnaMapper.deleteAnalysisCollectionItems(itemIds);
    }

    @Override
    public void deleteAnalysisCollection(Integer collectionId) {
        statsAnaMapper.deleteAnalysisCollectionById(collectionId);
    }

    @Override
    public void insertAnalysisCollectionItems(List patentIds, Integer collectionId) {
        statsAnaMapper.insertAnalysisCollectionItems(patentIds, collectionId);
    }

    @Override
    public void insertAnalysisCollection(String collectionName) {
        statsAnaMapper.insertAnalysisCollection(collectionName);
    }
}
