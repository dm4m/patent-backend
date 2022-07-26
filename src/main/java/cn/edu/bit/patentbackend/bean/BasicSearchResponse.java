package cn.edu.bit.patentbackend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicSearchResponse {
    Integer curPage;
    Long totalHits;
    Integer pageNum;
    Integer perPage;
    String query;
    String field;
    String searchType;
    List<Map<String, Object>> results;
}
