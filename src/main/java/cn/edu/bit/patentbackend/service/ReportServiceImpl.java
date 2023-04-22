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
    public void insertNoveltyResults(List noveltyAnalysisResult, String focusSigory, String resultName) {
        Integer word_pairs_sum = 0;
        Integer trigger_rules_sum = 0;
        Integer numeric_range_sum = 0;
        Integer hyponym_hypernym_sum = 0;
        Integer direct_substitution_sum = 0;
        Integer destroy_sum = 0;
        for (Object res : noveltyAnalysisResult) {
            HashMap resMap = (HashMap) res;
            HashMap statistical_dict = (HashMap) resMap.get("statistical_dict");
            word_pairs_sum += (Integer) statistical_dict.get("word_pairs");
            trigger_rules_sum += (Integer) statistical_dict.get("trigger_rules");
            numeric_range_sum += (Integer) statistical_dict.get("numeric_range");
            hyponym_hypernym_sum += (Integer) statistical_dict.get("hyponym_hypernym");
            direct_substitution_sum += (Integer) statistical_dict.get("direct_substitution");
            destroy_sum += (Integer) statistical_dict.get("destroy");
        }
//      statistical_info = f"共探测到{statistical_dict['word_pairs']}条相关关系词对；{statistical_dict['trigger_rules']}条新颖性评判规则相关点，其中数值和数值范围相关点{statistical_dict['numeric_range']}条、上下位概念相关点{statistical_dict['hyponym_hypernym']}条、惯用手段的直接置换相关点{statistical_dict['direct_substitution']}条。可能会破坏所提交发明的有{statistical_dict['destroy']}项。"
        String statistical_info_sum = "共探测到" + word_pairs_sum.toString() + "条相关关系词对；" +
                trigger_rules_sum.toString() + "条新颖性评判规则相关点，其中数值和数值范围相关点" + numeric_range_sum.toString() +
                "条、上下位概念相关点" + hyponym_hypernym_sum.toString() + "条、惯用手段的直接置换相关点" + direct_substitution_sum.toString()
                + "条。可能会破坏所提交发明的有" + destroy_sum.toString() + "项。";

        InsertOut insertOut = new InsertOut();
        reportMapper.createNewNoveltyResult(insertOut, focusSigory, resultName, word_pairs_sum, trigger_rules_sum
                , numeric_range_sum, hyponym_hypernym_sum, direct_substitution_sum, destroy_sum, statistical_info_sum);
        Integer noveltyResultId = insertOut.getId();
        reportMapper.insertNoveltyAnaItems(noveltyResultId, noveltyAnalysisResult);
//        String itemType = ReportContentType.Novelty;
//        reportMapper.insertRCItem(itemType, noveltyResultId, itemType + noveltyResultId.toString(), reportId);
    }

    @Override
    public ArrayList<Map<String, Object>> getSignorysById(Integer patentId) {
        return reportMapper.getSignorysById(patentId);
    }

    @Override
    @Transactional
    public void insertStatsResults(Integer reportId, List<String> options, Integer collectionId) {
        InsertOut insertOut = new InsertOut();
        reportMapper.createNewStatsResult(insertOut, collectionId);
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

    @Override
    public void addNoveltyResult2Report(Integer reportId, Integer noveltyResId, String noveltyAnaName) {
        String itemType = ReportContentType.Novelty;
        reportMapper.insertRCItem(itemType, noveltyResId, noveltyAnaName, reportId);
    }

    @Override
    public void addSearchResults2Report(Integer reportId, Integer collectionId, String collectionName) {
        String itemType = ReportContentType.Search;
        reportMapper.insertRCItem(itemType, collectionId, collectionName, reportId);
    }
}
