package cn.edu.bit.patentbackend.controller;

import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;
import cn.edu.bit.patentbackend.bean.Report2gen;
import cn.edu.bit.patentbackend.bean.ReportContentIitemRsp;
import cn.edu.bit.patentbackend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(path = "/report2gen", method = RequestMethod.POST)
    public void insertReport2gen(@RequestBody Map request)
    {
        String reportName = (String) request.get("reportName");
        reportService.insertReport2gen(reportName);
        return;
    }


    @RequestMapping(path = "/reportContentItem", method = RequestMethod.GET)
    public ReportContentIitemRsp getReportContentItems(@RequestParam("reportId") Integer reportId,
                                                       @RequestParam("pageIndex") Integer pageIndex,
                                                       @RequestParam("pageSize") Integer pageSize)
    {

        return reportService.getReportContentItems(reportId, pageIndex, pageSize);
    }

    @RequestMapping(path = "/reportContentItem", method = RequestMethod.DELETE)
    public void deleteReportContentCItemsByIds(@RequestBody Map request)
    {
        List itemIds = (List) request.get("itemIds");
        reportService.deleteReportContentItems(itemIds);
        return;
    }

    @RequestMapping(path = "/searchResults", method = RequestMethod.POST)
    public void insertSearchResults(@RequestBody Map request){
        List patentIds = (List) request.get("patentIds");
        Integer reportId = (Integer) request.get("reportId");
        reportService.insertSearchResults(patentIds, reportId);
        return;
    }

    @RequestMapping(path = "/noveltyResults", method = RequestMethod.POST)
    public void insertNoveltyResults(@RequestBody Map request){
        List noveltyAnalysisResult = (List) request.get("noveltyAnalysisResult");
        String focusSigory = (String) request.get("focusSigory");
        Integer reportId = (Integer) request.get("reportId");
        reportService.insertNoveltyResults(noveltyAnalysisResult, focusSigory, reportId);
        return;
    }

}
