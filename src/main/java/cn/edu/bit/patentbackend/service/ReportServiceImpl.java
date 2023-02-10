package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.*;
import cn.edu.bit.patentbackend.mapper.ReportMapper;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
