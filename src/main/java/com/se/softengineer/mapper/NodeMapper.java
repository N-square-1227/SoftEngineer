package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Node;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:42
 */
@Mapper
public interface NodeMapper extends BaseMapper<Node> {

    //@Options(useGeneratedKeys = true, keyColumn = "node_id")
    @Insert("insert into ${tableName} " +
            "(node_id, node_name, node_type, node_weight, parent_id) " +
            "values (#{nodeId}, #{nodeName}, #{nodeType}, #{nodeWeight}, #{parentID})")
    void insertIntoSheet(String tableName, Integer nodeId, @Param("nodeName") String nodeName,
                           @Param("nodeType") Integer nodeType, @Param("nodeWeight") Double noteWeight,
                           @Param("parentID") Integer parentID);
}