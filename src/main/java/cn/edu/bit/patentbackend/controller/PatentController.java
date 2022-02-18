package cn.edu.bit.patentbackend.controller;
import cn.edu.bit.patentbackend.bean.basicSearchResponse;
import cn.edu.bit.patentbackend.bean.mySQLSearchResponse;
import cn.edu.bit.patentbackend.service.PatentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/patent")
public class PatentController {

    @Autowired
    PatentService patentService;

    @RequestMapping(path = "/basicSearch", method = RequestMethod.GET)
    public basicSearchResponse basicSearch(@RequestParam("query") String query,
                                           @RequestParam("field") String field,
                                           @RequestParam("cur_page") Integer curPage,
                                           @RequestParam("per_page") Integer perPage
                                            ) throws IOException {
        basicSearchResponse response = patentService.basicSearch(query, field, curPage, perPage);
        // field字段名称应与ES后端字段名称保持一致
        return response;
    }

    @RequestMapping(path = "/neuralSearch", method = RequestMethod.GET)
    public basicSearchResponse neuralSearch(@RequestParam("query") String query,
                                            @RequestParam("cur_page") Integer curPage,
                                            @RequestParam("per_page") Integer perPage) throws JsonProcessingException {
        basicSearchResponse response = patentService.neuralSearch(query, curPage, perPage);
        return response;
    }

}
