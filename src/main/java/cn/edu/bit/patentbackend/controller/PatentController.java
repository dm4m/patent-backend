package cn.edu.bit.patentbackend.controller;
import cn.edu.bit.patentbackend.bean.AdvancedSearchRequset;
import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItemRsp;
import cn.edu.bit.patentbackend.bean.SearchResponse;
import cn.edu.bit.patentbackend.service.PatentService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patent")
public class PatentController {

    @Autowired
    PatentService patentService;

    @RequestMapping(path = "/basicSearch", method = RequestMethod.GET)
    public SearchResponse basicSearch(@RequestParam("query") String query,
                                      @RequestParam("field") String field,
                                      @RequestParam("cur_page") Integer curPage,
                                      @RequestParam("per_page") Integer perPage
                                            ) throws IOException {
        SearchResponse response = patentService.basicSearch(query, field, curPage, perPage);
        // field字段名称应与ES后端字段名称保持一致
        return response;
    }

    @RequestMapping(path = "/neuralSearch", method = RequestMethod.GET)
    public SearchResponse neuralSearch(@RequestParam("query") String query,
                                       @RequestParam("field") String field,
                                       @RequestParam("cur_page") Integer curPage,
                                       @RequestParam("per_page") Integer perPage) throws JsonProcessingException {
        SearchResponse response = patentService.neuralSearch(query, field, curPage, perPage);
        return response;
    }

    @RequestMapping(path = "/advancedSearch", method = RequestMethod.POST)
    public SearchResponse advancedSearch(@RequestBody AdvancedSearchRequset request) throws JsonProcessingException {
        System.out.println(request.toString());
        LinkedHashMap conditionMap = (LinkedHashMap) request.getConditionMap();
        int perPage = (int) request.getPerPage();
        int curPage = (int) request.getCurPage();
        SearchResponse response = patentService.advancedSearch(conditionMap, curPage, perPage);
        return response;
    }

    @RequestMapping(path = "/proSearch", method = RequestMethod.GET)
    public SearchResponse proSearch(@RequestParam("expression") String expression,
                                    @RequestParam("cur_page") Integer curPage,
                                    @RequestParam("per_page") Integer perPage) throws JsonProcessingException {
        SearchResponse response = patentService.proSearch(expression, curPage, perPage);
        return response;
    }

    @RequestMapping(path = "/uploadSearch", method = RequestMethod.POST)
    public SearchResponse uploadSearch(@RequestParam("file")MultipartFile file) {
        SearchResponse response = null;
        try {
            response = patentService.uploadSearch(file);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @RequestMapping(path = "/analysisCollection", method = RequestMethod.GET)
    public ArrayList<AnalysisCollection> getAnalysisCollection(){
        ArrayList<AnalysisCollection> res = patentService.getAnalysisCollection();
        return res;
    }

    @RequestMapping(path = "/analysisCollectionItem", method = RequestMethod.GET)
    public AnalysisCollectionItemRsp getAnalysisCollectionItems(@RequestParam("collectionId") Integer collectionId,
                                                                @RequestParam("pageIndex") Integer pageIndex,
                                                                @RequestParam("pageSize") Integer pageSize)
    {
        System.out.println(collectionId + " " + pageIndex + " " + pageSize);
        return patentService.getAnalysisCollectionItems(collectionId, pageIndex, pageSize);
    }

    @RequestMapping(path = "/analysisCollection", method = RequestMethod.DELETE)
    public void deleteAnalysisCollection(@RequestParam("collectionId")Integer collectionId)
    {
        patentService.deleteAnalysisCollection(collectionId);
        return;
    }

    @RequestMapping(path = "/analysisCollection", method = RequestMethod.POST)
    public void insertAnalysisCollection(@RequestBody Map request)
    {
        String collectionName = (String) request.get("collectionName");
        patentService.insertAnalysisCollection(collectionName);
        return;
    }

    @RequestMapping(path = "/analysisCollectionItem", method = RequestMethod.DELETE)
    public void deleteAnalysisCollectionItems(@RequestBody Map request)
    {

        List itemIds = (List) request.get("itemIds");
        patentService.deleteAnalysisCollectionItems(itemIds);
        return;
    }

    @RequestMapping(path = "/analysisCollectionItem", method = RequestMethod.POST)
    public void insertAnalysisCollectionItems(@RequestBody Map request)
    {
        List patentIds = (List) request.get("patentIds");
        Integer collectionId = (Integer) request.get("collectionId");
        patentService.insertAnalysisCollectionItems(patentIds, collectionId);
        System.out.println(collectionId);
        return;
    }


}
