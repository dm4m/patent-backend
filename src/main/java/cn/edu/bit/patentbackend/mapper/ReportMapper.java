package cn.edu.bit.patentbackend.mapper;

import cn.edu.bit.patentbackend.bean.AnalysisCollection;
import cn.edu.bit.patentbackend.bean.AnalysisCollectionItem;
import cn.edu.bit.patentbackend.bean.Report2gen;
import cn.edu.bit.patentbackend.bean.ReportContentItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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

}
