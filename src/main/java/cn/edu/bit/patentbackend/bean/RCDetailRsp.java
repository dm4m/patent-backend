package cn.edu.bit.patentbackend.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
public class RCDetailRsp {
    List<Map<String, Object>> searchResults;

    List<Map<String, Object>> statsResults;

    Map<String, Object> noveltyAnaResult;

    List<Map<String, Object>> noveltyStatsResults;

    List<String> reportSignorys;
}
