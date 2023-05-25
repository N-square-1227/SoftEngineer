package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.se.softengineer.entity.IndexSymNode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:42
 */
@Mapper
public interface IndexSymMapper extends BaseMapper<IndexSymNode> {

    @Insert("insert into ${tableName} " +
            "( node_name, node_type, node_weight, parent_id) " +
            "values ( #{nodeName}, #{nodeType}, #{nodeWeight}, #{parentID})")
    boolean insertIntoTable(String tableName, @Param("nodeName") String nodeName,
                            @Param("nodeType") Integer nodeType, @Param("nodeWeight") Double noteWeight,
                            @Param("parentID") Integer parentID);

    @Insert("insert into ${tableName} " +
            "(node_id, node_name, node_type, node_weight, parent_id) " +
            "values ( #{nodeID}, #{nodeName}, #{nodeType}, #{nodeWeight}, #{parentID})")
    boolean insertIntoTable_noauto(String tableName,  @Param("nodeID") Integer nodeID, @Param("nodeName") String nodeName,
                                   @Param("nodeType") Integer nodeType, @Param("nodeWeight") Double noteWeight,
                                   @Param("parentID") Integer parentID);

    List<IndexSymNode> getIndex(String table_name);

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

    /**
     * 分页查询指标体系数据表
     **/
    IPage getListPage(IPage<IndexSymNode> page, @Param("table_name") String table_name, @Param(Constants.WRAPPER) Wrapper wrapper);
}