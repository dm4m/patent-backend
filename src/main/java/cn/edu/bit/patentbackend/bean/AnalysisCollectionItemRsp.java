package cn.edu.bit.patentbackend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnalysisCollectionItemRsp {
    List<AnalysisCollectionItem> itemList;

    Integer totalItemCount;
}
