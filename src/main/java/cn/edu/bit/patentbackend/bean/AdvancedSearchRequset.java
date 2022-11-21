package cn.edu.bit.patentbackend.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class AdvancedSearchRequset {
    @JsonProperty(value = "cur_page")
    int curPage;

    @JsonProperty(value = "per_page")
    int perPage;

    @JsonProperty(value = "conditions")
    LinkedHashMap<Integer, AdvancedSearchCondition> conditionMap;
}
