package cn.edu.bit.patentbackend.bean;
import lombok.Data;

@Data
public class AnalysisCollectionItem {
    Integer itemId;
    Integer patentId;
    String patentName;
    String inventor;
}
