package cn.edu.bit.patentbackend.service;

import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;
import cn.edu.bit.patentbackend.bean.Report2gen;
import cn.edu.bit.patentbackend.bean.ReportContentIitemRsp;

import java.util.ArrayList;

public interface ReportService {
    ArrayList<Report2gen> getReport2gen();

    void deleteReport2gen(Integer reportId);


    ReportContentIitemRsp getReportContentItems(Integer reportId, Integer pageIndex, Integer pageSize);
}
