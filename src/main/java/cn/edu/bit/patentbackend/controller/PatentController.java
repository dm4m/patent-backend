package cn.edu.bit.patentbackend.controller;
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
    public List<Map<String, Object>> search(@RequestParam("query") String query) throws IOException {
        ArrayList<Map<String, Object>> result = patentService.search(query);
        return result;
    }
}
