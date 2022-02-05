package cn.edu.bit.patentbackend.controller;
import cn.edu.bit.patentbackend.bean.SearchResponse;
import cn.edu.bit.patentbackend.service.PatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patent")
public class PatentController {

    @Autowired
    PatentService patentService;

    @RequestMapping(method = RequestMethod.GET)
    public SearchResponse search(@RequestParam("query") String query,
                                 @RequestParam("cur_page") Integer curPage,
                                 @RequestParam("per_page") Integer perPage,
                                 @RequestParam("field") String field
                                            ) throws IOException {
        SearchResponse response = new SearchResponse();
        // field字段名称应与ES后端字段名称保持一致
        response = patentService.search(query, curPage, perPage, field);
        return response;
    }
}
