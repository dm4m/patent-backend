package cn.edu.bit.patentbackend.mapper;

import cn.edu.bit.patentbackend.bean.InsertOut;
import cn.edu.bit.patentbackend.bean.Report2gen;
import cn.edu.bit.patentbackend.bean.ReportContentItem;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

public interface ReportMapper {
    @Results(value = {
            @Result(property = "reportId", column = "report_id", id = true),
            @Result(property = "reportName", column = "report_name")}
    )
    @Select("SELECT report_id, report_name FROM report2generate")
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
            "insert into novelty_ana_result(ori_signory) VALUES(#{focusSigory});"
    })
    @Options(useGeneratedKeys=true, keyColumn="novelty_ana_id", keyProperty = "insertOut.id")
    void createNewNoveltyResult(InsertOut insertOut, String focusSigory);

    @Insert({
            "<script>"  +
                    "insert IGNORE INTO novelty_ana_item(novelty_ana_id, relevant_sig, compare_result, ori_patent_title) VALUES" +
                    "<foreach collection='noveltyAnalysisResults' item='result' separator=','> " +
                    "(#{noveltyResultId}, #{result.relevant_sig}, #{result.compare_result}, #{result.ori_patent_title})" +
                    "</foreach>" +
                    "</script>"
    })
    void insertNoveltyAnaItems(Integer noveltyResultId, List noveltyAnalysisResults);

    @Select("SELECT signory_item FROM signory where patent_id = #{patentId}")
    ArrayList<String> getSignorysById(Integer patentId);
}
