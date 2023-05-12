package com.se.softengineer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.algorithm.indexsym.Node;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:42
 */
@Mapper
public interface NodeMapper extends BaseMapper<Node> {

    @Options(useGeneratedKeys = true, keyColumn = "node_id")
    @Insert("insert into se.en_indexsym(node_name, node_type, node_weight, parent_id) values (#{nodeName}, #{nodeType}, #{nodeWeight}, #{parentID})")
    void insertIntoEntropy(String tableName, @Param("nodeName") String nodeName,
                           @Param("nodeType") Integer nodeType, @Param("nodeWeight") Double noteWeight,
                           @Param("parentID") Integer parentID);
}