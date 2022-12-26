package cn.edu.bit.patentbackend.controller;
import cn.edu.bit.patentbackend.bean.AdvancedSearchRequset;
import cn.edu.bit.patentbackend.bean.SearchResponse;
import cn.edu.bit.patentbackend.service.PatentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.LinkedHashMap;

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

}
