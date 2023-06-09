package cn.edu.bit.patentbackend.mapper;
import cn.edu.bit.patentbackend.bean.Patent;
import cn.edu.bit.patentbackend.bean.PatentWithBLOBs;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PatentMapper {
    PatentWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PatentWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PatentWithBLOBs record);

    int updateByPrimaryKey(Patent record);

    List<Map<String, Object>> selectPatentListByIds(@Param("Ids") List<Integer> Ids);

}