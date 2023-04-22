package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItem;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;
import cn.edu.bit.patentbackend.bean.stats.NoveltyAnaResult;
import cn.edu.bit.patentbackend.bean.stats.NoveltyAnaResultItem;
import cn.edu.bit.patentbackend.bean.stats.NoveltyAnaResultItemRsp;
import cn.edu.bit.patentbackend.mapper.ReportMapper;
import cn.edu.bit.patentbackend.mapper.StatsAnaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class StatsAnaServiceImpl implements StatsAnaService{

    @Autowired
    StatsAnaMapper statsAnaMapper;

    @Autowired
    ReportMapper reportMapper;

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

    @Override
    public ArrayList<NoveltyAnaResult> getNoveltyAnaResults() {
        return (ArrayList<NoveltyAnaResult>) statsAnaMapper.getNoveltyAnaResults();
    }

    @Override
    public NoveltyAnaResultItemRsp getNoveltyAnaResultItems(Integer noveltyAnaId, Integer pageIndex, Integer pageSize) {
        List<NoveltyAnaResultItem> noveltyAnaResultItems = statsAnaMapper.getNoveltyAnaResultItems(noveltyAnaId, pageSize, pageIndex * pageSize);
        Integer naItemsAccount = statsAnaMapper.getNAItemsAccount(noveltyAnaId);
        NoveltyAnaResultItemRsp noveltyAnaResultItemRsp = new NoveltyAnaResultItemRsp(noveltyAnaResultItems, naItemsAccount);
        return noveltyAnaResultItemRsp;
    }

    @Override
    @Transactional
    public void deleteNoveltyRes(Integer noveltyResId) {
        reportMapper.deleteReportContentItemByCorrId(noveltyResId);
        statsAnaMapper.deleteNoveltyResItemsByResId(noveltyResId);
        statsAnaMapper.deleteNoveltyResById(noveltyResId);
    }

}
