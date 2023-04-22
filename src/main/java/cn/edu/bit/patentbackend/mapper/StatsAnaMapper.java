package cn.edu.bit.patentbackend.mapper;

import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItem;
import cn.edu.bit.patentbackend.bean.stats.NoveltyAnaResult;
import cn.edu.bit.patentbackend.bean.stats.NoveltyAnaResultItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface StatsAnaMapper {
    @Results(id = "userResult", value = {
            @Result(property = "collection_id", column = "collection_id", id = true),
            @Result(property = "name", column = "name")
    })
    @Select("SELECT collection_id, name FROM analysis_collection")
    List<AnalysisCollection> getAllAnalysisCollection();

    @Select("SELECT item_id as itemId, title as patentName, inventor_list as inventor from analysis_collection_item, patent " +
            "where collection_id = #{collectionId} and patent_id = patent.id " +
            "limit #{limit} offset #{offset}")
    List<AnalysisCollectionItem> getAnalysisCollectionItems(Integer collectionId, Integer limit, Integer offset);

    @Select("SELECT count(*) from analysis_collection_item, patent " +
            "where collection_id = #{collectionId} and patent_id = patent.id ")
    Integer getACItemsAccount(Integer collectionId);

    @Delete({
            "<script>"  +
                    "delete from analysis_collection_item where item_id in " +
                    "<foreach collection='itemIds' item='itemId' open='(' separator=',' close=')'> " +
                    "#{itemId}" +
                    "</foreach>" +
                    "</script>"
    })
    void deleteAnalysisCollectionItems(List<Integer> itemIds);

    @Delete({
            "delete from analysis_collection where collection_id = #{collectionId}"
    })
    void deleteAnalysisCollectionById(Integer collectionId);

    @Insert({
            "<script>"  +
                    "insert IGNORE INTO analysis_collection_item(collection_id, patent_id) VALUES" +
                    "<foreach collection='patentIds' item='patentId' separator=','> " +
                    "(#{collectionId}, #{patentId})" +
                    "</foreach>" +
                    "</script>"
    })
    void insertAnalysisCollectionItems(List patentIds, Integer collectionId);

    @Insert({
            "insert INTO analysis_collection(name) VALUES (#{collectionName})"
    })
    void insertAnalysisCollection(String collectionName);

    @Results(
            value = {
            @Result(property = "novelty_ana_id", column = "novelty_ana_id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "ori_signory", column = "ori_signory"),
    })
    @Select("SELECT novelty_ana_id, name, ori_signory FROM novelty_ana_result")
    List<NoveltyAnaResult> getNoveltyAnaResults();

    @Results(
            value = {
                    @Result(property = "novelty_ana_item_id", column = "novelty_ana_item_id", id = true),
                    @Result(property = "novelty_ana_id", column = "novelty_ana_id"),
                    @Result(property = "relevant_sig", column = "relevant_sig"),
                    @Result(property = "compare_result", column = "compare_result"),
                    @Result(property = "ori_patent_title", column = "ori_patent_title")
            })
    @Select("SELECT novelty_ana_item_id as novelty_ana_item_id, " +
            "novelty_ana_id as novelty_ana_id, " +
            "relevant_sig as relevant_sig,  " +
            "compare_result as compare_result, " +
            "ori_patent_title as ori_patent_title " +
            "from novelty_ana_item " +
            "where novelty_ana_id = #{noveltyAnaId} " +
            "limit #{limit} offset #{offset}")
    List<NoveltyAnaResultItem> getNoveltyAnaResultItems(Integer noveltyAnaId, Integer limit, int offset);

    @Select("SELECT count(*) from novelty_ana_item " +
            "where novelty_ana_id = #{noveltyAnaId} ")
    Integer getNAItemsAccount(Integer noveltyAnaId);

    @Delete({
            "delete from novelty_ana_result where novelty_ana_id = #{noveltyResId}"
    })
    void deleteNoveltyResById(Integer noveltyResId);

    @Delete({
            "delete from novelty_ana_item where novelty_ana_id = #{noveltyResId}"
    })
    void deleteNoveltyResItemsByResId(Integer noveltyResId);
}
