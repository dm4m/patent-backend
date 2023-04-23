package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.RCDetailRsp;
import cn.edu.bit.patentbackend.bean.Report2gen;
import cn.edu.bit.patentbackend.bean.ReportContentIitemRsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ReportService {
    ArrayList<Report2gen> getReport2gen();

    void deleteReport2gen(Integer reportId);


    ReportContentIitemRsp getReportContentItems(Integer reportId, Integer pageIndex, Integer pageSize);

    void deleteReportContentItems(List itemIds);

    void insertReport2gen(String reportName);

    void insertSearchResults(List patentIds, Integer reportId);

    void insertNoveltyResults(List noveltyAnalysisResult, String focusSigory, String reportId);

    ArrayList<Map<String, Object>> getSignorysById(Integer patentId);

    void insertStatsResults(Integer reportId, List<String> options, Integer collectionId);

    RCDetailRsp getReportContentDetail(String itemType, Integer corrId);

    void addNoveltyResult2Report(Integer reportId, Integer noveltyResId, String noveltyAnaName);

    void addSearchResults2Report(Integer reportId, Integer collectionId, String collectionName);

    void saveAndAddNoveltyStats(Integer reportId, List<String> options, Integer noveltyResId);
}
