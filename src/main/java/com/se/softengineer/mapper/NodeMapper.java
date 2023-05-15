package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Node;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:42
 */
@Mapper
public interface NodeMapper extends BaseMapper<Node> {

    @Insert("insert into ${tableName} " +
            "(node_id, node_name, node_type, node_weight, parent_id) " +
            "values (#{nodeId}, #{nodeName}, #{nodeType}, #{nodeWeight}, #{parentID})")
    void insertIntoSheet(String tableName, Integer nodeId, @Param("nodeName") String nodeName,
                           @Param("nodeType") Integer nodeType, @Param("nodeWeight") Double noteWeight,
                           @Param("parentID") Integer parentID);

    List<Node> getIndex(String table_name);

    /**
     * 南希诺
     * 创建熵权法表
     *             "`node_id` int(11) NOT NULL AUTO_INCREMENT, " +
     * todo：修改前端的 tableName！
     * @param tableName 后端修改后的表名
     */
    void createTable(@Param("tableName") String tableName);

    /**
     * 南希诺
     * 删除表
     * @param tableName 后端修改后的表名
     */
    void dropExistTable(@Param("tableName") String tableName);
}