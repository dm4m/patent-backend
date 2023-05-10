package cn.edu.bit.patentbackend.mapper;

import cn.edu.bit.patentbackend.bean.InsertOut;
import cn.edu.bit.patentbackend.bean.Report2gen;
import cn.edu.bit.patentbackend.bean.ReportContentItem;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ReportMapper {
    @Results(value = {
            @Result(property = "reportId", column = "report_id", id = true),
            @Result(property = "reportName", column = "report_name"),
            @Result(property = "status", column = "status"),
            @Result(property = "pdfFilePath", column = "pdf_file_path")}
    )
    @Select("SELECT report_id, report_name, status, pdf_file_path FROM report2generate")
    List<Report2gen> getAllReport2gen();

    @Delete({
            "delete from report2generate where report_id = #{reportId}"
    })
    void deleteReport2genById(Integer reportId);

    @Select("SELECT report_item_id as reportItemId, item_type as itemType, corr_id as corrId, name from report_content_item " +
            "where report_id = #{reportId} " +
            "limit #{limit} offset #{offset}")
    List<ReportContentItem>  getReportContentItems(Integer reportId, Integer limit, Integer offset);

    @Select("SELECT count(*) from report_content_item " +
            "where report_id = #{reportId} ")
    Integer getRCItemsAccount(Integer reportId);

    @Delete({
            "<script>"  +
                    "delete from report_content_item where report_item_id in " +
                    "<foreach collection='itemIds' item='itemId' open='(' separator=',' close=')'> " +
                    "#{itemId}" +
                    "</foreach>" +
                    "</script>"
    })
    void deleteReportContentItems(List<Integer> itemIds);

    @Insert({
            "insert INTO report2generate(report_name) VALUES (#{reportName})"
    })
    void insertReport2gen(String reportName);

    @Insert({
            "insert into search_result() VALUES();"
    })
    @Options(useGeneratedKeys=true, keyColumn="search_result_item_id", keyProperty = "id")
    Integer createNewSearchResult(InsertOut insertOut);


    @Insert({
            "<script>"  +
                    "insert IGNORE INTO search_result_item(search_result_id, patent_id) VALUES" +
                    "<foreach collection='patentIds' item='patentId' separator=','> " +
                    "(#{searchResultId}, #{patentId})" +
                    "</foreach>" +
                    "</script>"
    })
    void insertSearchResultItems(Integer searchResultId, List patentIds);


    @Insert({
            "insert INTO report_content_item(item_type, corr_id, name, report_id) " +
                    "VALUES (#{itemType}, #{searchResultId}, #{name}, #{reportId})"
    })
    void insertRCItem(String itemType, Integer searchResultId, String name, Integer reportId);

    @Insert({
            "insert into novelty_ana_result(ori_signory, name, word_pairs_sum, trigger_rules_sum, numeric_range_sum, hyponym_hypernym_sum, " +
                    "direct_substitution_sum, destroy_sum, statistical_info_sum) VALUES(#{focusSigory}, #{resultName}, " +
                    "#{word_pairs_sum}, #{trigger_rules_sum}, #{numeric_range_sum}, #{hyponym_hypernym_sum}, #{direct_substitution_sum}, #{destroy_sum}, #{statistical_info_sum});"
    })
    @Options(useGeneratedKeys=true, keyColumn="novelty_ana_id", keyProperty = "insertOut.id")
    void createNewNoveltyResult(InsertOut insertOut, String focusSigory, String resultName,
                                Integer word_pairs_sum, Integer trigger_rules_sum,
                                Integer numeric_range_sum, Integer hyponym_hypernym_sum,
                                Integer direct_substitution_sum, Integer destroy_sum, String statistical_info_sum);

    @Insert({
            "<script>"  +
                "insert IGNORE INTO novelty_ana_item(novelty_ana_id, relevant_sig, compare_result, ori_patent_title, " +
                    "word_pairs, trigger_rules, numeric_range, hyponym_hypernym, direct_substitution, destroy, statistical_info, index_num, score, patent_code) VALUES" +
                "<foreach collection='noveltyAnalysisResults' item='result' separator=','>" +
                    "(#{noveltyResultId}, #{result.relevant_sig}, #{result.compare_result}, #{result.ori_patent_title}, " +
                    "#{result.statistical_dict.word_pairs}, #{result.statistical_dict.trigger_rules}, #{result.statistical_dict.numeric_range}, " +
                    "#{result.statistical_dict.hyponym_hypernym}, #{result.statistical_dict.direct_substitution}, #{result.statistical_dict.destroy}, " +
                    "#{result.statistical_info}, #{result.index_num}, #{result.score}, #{result.patent_code})" +
                "</foreach>" +
            "</script>"
    })
    void insertNoveltyAnaItems(Integer noveltyResultId, List noveltyAnalysisResults);

    @Insert({
        "insert into stats_ana_result(analysis_collection_id) VALUES(#{collectionId});"
    })
    @Options(useGeneratedKeys=true, keyColumn="stats_res_id", keyProperty = "insertOut.id")
    void createNewStatsResult(InsertOut insertOut, Integer collectionId);

    @Insert({
        "<script>"  +
            "insert IGNORE INTO stats_ana_item(stats_ana_id, option_json) VALUES" +
            "<foreach collection='options' item='option' separator=','> " +
                "(#{statsResultId}, #{option})" +
            "</foreach>" +
        "</script>"
    })
    void insertStatsAnaItems(Integer statsResultId, List<String> options);

    @Select("SELECT option_json from stats_ana_item")
    List<String> selectOptions();

    @Select("SELECT signory_id, signory_item FROM signory where patent_id = #{patentId}")
    ArrayList<Map<String, Object>> getSignorysById(Integer patentId);

    @Select("SELECT search_result_item_id, patent_id FROM search_result_item where search_result_id = #{searchResultId}")
    List<Map<String, Object>> getSearchResultItems(Integer searchResultId);

    @Select("SELECT stats_ana_item_id, option_json FROM stats_ana_item where stats_ana_id = #{statsAnaId}")
    List<Map<String, Object>> getStatsAnaItems(Integer statsAnaId);
    @Select("SELECT ori_signory FROM novelty_ana_result where novelty_ana_id = #{noveltyAnaId}")
    String getOriSignory(Integer noveltyAnaId);

    @Select("SELECT novelty_ana_item_id, relevant_sig, ori_patent_title, compare_result, score, index_num FROM novelty_ana_item where novelty_ana_id = #{noveltyAnaId}")
    List<Map<String, Object>> getNoveltyAnaItems(Integer noveltyAnaId);

    @Delete("delete from report_content_item where corr_id = #{corrId}")
    void deleteReportContentItemByCorrId(Integer corrId);

    @Insert({
            "insert into novelty_stats_result(novelty_ana_result_id) VALUES(#{noveltyResId});"
    })
    @Options(useGeneratedKeys=true, keyColumn="id", keyProperty = "insertOut.id")
    void createNewNoveltyStatsResult(InsertOut insertOut, Integer noveltyResId);

    @Insert({
            "<script>"  +
                    "insert IGNORE INTO novelty_stats_item(novelty_stats_id, option_json) VALUES" +
                    "<foreach collection='options' item='option' separator=','> " +
                    "(#{noveltyStatsResultId}, #{option})" +
                    "</foreach>" +
                    "</script>"
    })
    void insertNoveltyStatsAnaItems(Integer noveltyStatsResultId, List<String> options);

    @Select("SELECT novelty_stats_item_id, option_json FROM novelty_stats_item where novelty_stats_id = #{corrId}")
    List<Map<String, Object>> getNoveltyStatsAnaItems(Integer corrId);
}
