package cn.edu.bit.patentbackend.service;
import cn.edu.bit.patentbackend.bean.*;
import cn.edu.bit.patentbackend.mapper.ReportMapper;
import cn.edu.bit.patentbackend.utils.ReportContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    ReportMapper reportMapper;

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
}
