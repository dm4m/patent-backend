package cn.edu.bit.patentbackend.controller;

import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;
import cn.edu.bit.patentbackend.bean.Report2gen;
import cn.edu.bit.patentbackend.bean.ReportContentIitemRsp;
import cn.edu.bit.patentbackend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @RequestMapping(path = "/report2gen", method = RequestMethod.GET)
    public ArrayList<Report2gen> getReport2gen(){
        return reportService.getReport2gen();
    }

    @RequestMapping(path = "/report2gen", method = RequestMethod.DELETE)
    public void deleteReport2gen(@RequestParam("reportId")Integer reportId)
    {
        reportService.deleteReport2gen(reportId);
        return;
    }

    @RequestMapping(path = "/reportContentItem", method = RequestMethod.GET)
    public ReportContentIitemRsp getReportContentItems(@RequestParam("reportId") Integer reportId,
                                                       @RequestParam("pageIndex") Integer pageIndex,
                                                       @RequestParam("pageSize") Integer pageSize)
    {

        return reportService.getReportContentItems(reportId, pageIndex, pageSize);
    }

}
