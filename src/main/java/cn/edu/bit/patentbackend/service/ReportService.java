package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.Report2gen;
import cn.edu.bit.patentbackend.bean.ReportContentIitemRsp;

import java.util.ArrayList;
import java.util.List;

public interface ReportService {
    ArrayList<Report2gen> getReport2gen();

    void deleteReport2gen(Integer reportId);


    ReportContentIitemRsp getReportContentItems(Integer reportId, Integer pageIndex, Integer pageSize);

    void deleteReportContentItems(List itemIds);

    void insertReport2gen(String reportName);

    void insertSearchResults(List patentIds, Integer reportId);

    void insertNoveltyResults(List noveltyAnalysisResult, String focusSigory, Integer reportId);

    ArrayList<String> getSignorysById(Integer patentId);
}
