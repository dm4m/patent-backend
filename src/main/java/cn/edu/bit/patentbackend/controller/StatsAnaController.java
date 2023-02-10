package cn.edu.bit.patentbackend.controller;

import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;
import cn.edu.bit.patentbackend.service.StatsAnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsAnaController {

    @Autowired
    StatsAnaService statsAnaService;

    @RequestMapping(path = "/analysisCollection", method = RequestMethod.GET)
    public ArrayList<AnalysisCollection> getAnalysisCollection(){
        ArrayList<AnalysisCollection> res = statsAnaService.getAnalysisCollection();
        return res;
    }

    @RequestMapping(path = "/analysisCollectionItem", method = RequestMethod.GET)
    public AnalysisCollectionItemRsp getAnalysisCollectionItems(@RequestParam("collectionId") Integer collectionId,
                                                                @RequestParam("pageIndex") Integer pageIndex,
                                                                @RequestParam("pageSize") Integer pageSize)
    {
        System.out.println(collectionId + " " + pageIndex + " " + pageSize);
        return statsAnaService.getAnalysisCollectionItems(collectionId, pageIndex, pageSize);
    }

    @RequestMapping(path = "/analysisCollection", method = RequestMethod.DELETE)
    public void deleteAnalysisCollection(@RequestParam("collectionId")Integer collectionId)
    {
        statsAnaService.deleteAnalysisCollection(collectionId);
        return;
    }

    @RequestMapping(path = "/analysisCollection", method = RequestMethod.POST)
    public void insertAnalysisCollection(@RequestBody Map request)
    {
        String collectionName = (String) request.get("collectionName");
        statsAnaService.insertAnalysisCollection(collectionName);
        return;
    }

    @RequestMapping(path = "/analysisCollectionItem", method = RequestMethod.DELETE)
    public void deleteAnalysisCollectionItems(@RequestBody Map request)
    {

        List itemIds = (List) request.get("itemIds");
        statsAnaService.deleteAnalysisCollectionItems(itemIds);
        return;
    }

    @RequestMapping(path = "/analysisCollectionItem", method = RequestMethod.POST)
    public void insertAnalysisCollectionItems(@RequestBody Map request)
    {
        List patentIds = (List) request.get("patentIds");
        Integer collectionId = (Integer) request.get("collectionId");
        statsAnaService.insertAnalysisCollectionItems(patentIds, collectionId);
        System.out.println(collectionId);
        return;
    }
}
