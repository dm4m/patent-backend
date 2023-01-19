package cn.edu.bit.patentbackend.mapper;
import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItem;
import cn.edu.bit.patentbackend.bean.Patent;
import cn.edu.bit.patentbackend.bean.PatentWithBLOBs;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface PatentMapper {
    PatentWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PatentWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PatentWithBLOBs record);

    int updateByPrimaryKey(Patent record);

    List<Map<String, Object>> selectPatentListByIds(@Param("Ids") List<Integer> Ids);

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
}