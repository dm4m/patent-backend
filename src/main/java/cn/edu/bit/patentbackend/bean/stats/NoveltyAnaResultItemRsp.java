package cn.edu.bit.patentbackend.bean.stats;

import cn.edu.bit.patentbackend.bean.AnalysisCollectionItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NoveltyAnaResultItemRsp {
    List<NoveltyAnaResultItem> itemList;

    Integer totalItemCount;
}
