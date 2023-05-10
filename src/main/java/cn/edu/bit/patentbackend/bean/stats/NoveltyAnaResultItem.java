package cn.edu.bit.patentbackend.bean.stats;

import lombok.Data;

@Data
public class NoveltyAnaResultItem {
    Integer novelty_ana_item_id;
    Integer novelty_ana_id;
    String relevant_sig;
    String compare_result;
    String ori_patent_title;
    String score;
    Integer index_num;
}
