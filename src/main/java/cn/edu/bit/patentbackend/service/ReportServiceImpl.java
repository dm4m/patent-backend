package cn.edu.bit.patentbackend.service;
import cn.edu.bit.patentbackend.bean.*;
import cn.edu.bit.patentbackend.mapper.ReportMapper;
import cn.edu.bit.patentbackend.mapper.PatentMapper;
import cn.edu.bit.patentbackend.utils.ReportContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    PatentMapper patentMapper;

    @Override
    public ArrayList<Report2gen> getReport2gen() {
        return (ArrayList<Report2gen>) reportMapper.getAllReport2gen();
    }

    @Override
    public void deleteReport2gen(Integer reportId) {
        reportMapper.deleteReport2genById(reportId);
    }

    @Override
    public ReportContentIitemRsp getReportContentItems(Integer reportId, Integer pageIndex, Integer pageSize) {
        List<ReportContentItem> reportContentItemList = reportMapper.getReportContentItems(reportId, pageSize, pageIndex * pageSize);
        Integer rcItemsAccount = reportMapper.getRCItemsAccount(reportId);
        ReportContentIitemRsp reportContentIitemRsp = new ReportContentIitemRsp(reportContentItemList, rcItemsAccount);
        return reportContentIitemRsp;
    }

    @Override
    public void deleteReportContentItems(List itemIds) {
        reportMapper.deleteReportContentItems(itemIds);
    }

    @Override
    public void insertReport2gen(String reportName) {
        reportMapper.insertReport2gen(reportName);
        return;
    }

    @Override
    @Transactional
    public void insertSearchResults(List patentIds, Integer reportId) {
        InsertOut insertOut = new InsertOut();
        reportMapper.createNewSearchResult(insertOut);
        Integer searchResultId = insertOut.getId();
        reportMapper.insertSearchResultItems(searchResultId, patentIds);
        String itemType = ReportContentType.Search;
        reportMapper.insertRCItem(itemType, searchResultId, itemType + searchResultId.toString(), reportId);
    }

    @Override
    @Transactional
    public void insertNoveltyResults(List noveltyAnalysisResult, String focusSigory, Integer reportId) {
        InsertOut insertOut = new InsertOut();
        reportMapper.createNewNoveltyResult(insertOut, focusSigory);
        Integer noveltyResultId = insertOut.getId();
        reportMapper.insertNoveltyAnaItems(noveltyResultId, noveltyAnalysisResult);
        String itemType = ReportContentType.Novelty;
        reportMapper.insertRCItem(itemType, noveltyResultId, itemType + noveltyResultId.toString(), reportId);
    }

    @Override
    public ArrayList<Map<String, Object>> getSignorysById(Integer patentId) {
        return reportMapper.getSignorysById(patentId);
    }

    @Override
    @Transactional
    public void insertStatsResults(Integer reportId, List<String> options) {
        InsertOut insertOut = new InsertOut();
        reportMapper.createNewStatsResult(insertOut);
        Integer noveltyStatsResultId = insertOut.getId();
        reportMapper.insertStatsAnaItems(noveltyStatsResultId, options);
        String itemType = ReportContentType.Stats;
        reportMapper.insertRCItem(itemType, noveltyStatsResultId, itemType + noveltyStatsResultId.toString(), reportId);
    }

    @Override
    public RCDetailRsp getReportContentDetail(String itemType, Integer corrId) {
        RCDetailRsp rcDetailRsp = new RCDetailRsp();
        if(itemType.equals("检索结果")){
            List<Map<String, Object>> searchResultItems = reportMapper.getSearchResultItems(corrId);
            List<Integer> searchResultsIds = new ArrayList<>();
            for (Map<String, Object> searchResultItem : searchResultItems) {
                searchResultsIds.add(((Long)searchResultItem.get("patent_id")).intValue());
            }
            List<Map<String, Object>> patents = patentMapper.selectPatentListByIds(searchResultsIds);
            for (int i = 0; i < patents.size(); i++) {
                patents.get(i).put("search_result_item_id", searchResultItems.get(i).get("search_result_item_id"));
            }
            rcDetailRsp.setSearchResults(patents);
        }else if(itemType.equals("统计分析结果")){
            List<Map<String, Object>> statsAnaItems = reportMapper.getStatsAnaItems(corrId);
            rcDetailRsp.setStatsResults(statsAnaItems);
        }else if(itemType.equals("新颖性比对结果")){
            HashMap<String, Object> noveltyAnaResult = new HashMap<>();
            String oriSig = reportMapper.getOriSignory(corrId);
            List<Map<String, Object>> noveltyAnaItems = reportMapper.getNoveltyAnaItems(corrId);
            noveltyAnaResult.put("ori_signory", oriSig);
            noveltyAnaResult.put("anaItems", noveltyAnaItems);
            rcDetailRsp.setNoveltyAnaResult(noveltyAnaResult);
        }
        return rcDetailRsp;
    }
}
