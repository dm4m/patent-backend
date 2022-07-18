package cn.edu.bit.patentbackend.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdvancedSearchCondition {
    @JsonProperty(value = "currentFieldOption")
    String field;

    @JsonProperty(value = "currentLogicOption")
    String logicOp;

    @JsonProperty(value = "currentMatchOption")
    String matchType;

    @JsonProperty(value = "text")
    String queryText;
}
