package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity. IndexSymNode;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author Nanxi
 * @create 2023/5/11 20:42
 */
@Mapper
public interface IndexSymNodeMapper extends BaseMapper<IndexSymNode> {

    //@Options(useGeneratedKeys = true, keyColumn = "node_id")
    @Insert("insert into ${tableName} " +
            "(node_id, node_name, node_type, node_weight, parent_id) " +
            "values (#{nodeId}, #{nodeName}, #{nodeType}, #{nodeWeight}, #{parentID})")
    void insertIntoSheet(String tableName, Integer nodeId, @Param("nodeName") String nodeName,
                         @Param("nodeType") Integer nodeType, @Param("nodeWeight") Double noteWeight,
                         @Param("parentID") Integer parentID);

    List< IndexSymNode> getIndex(String table_name);

    /**
     * 南希诺
     * 创建熵权法表
     */
    void createTable(@Param("tableName") String tableName);

    /**
     * 南希诺
     * 删除表
     * @param tableName 后端修改后的表名
     */
    void dropExistTable(@Param("tableName") String tableName);


    int getHeadID(@Param("table_name")String table_name, @Param("name") String name);

    /**
     * @author xy
     */
    @Update({"create table ${tableName}(" +
            "  `node_id` int NOT NULL AUTO_INCREMENT," +
            "  `node_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL," +
            "  `node_type` int NOT NULL," +
            "  `node_weight` double NOT NULL," +
            "  `parent_id` int NOT NULL," +
            "  PRIMARY KEY (`node_id`) USING BTREE" +
            ")" +
            "ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;"
    })
    int createIndexSymTable(@Param("tableName") String tableName);

    /**
     * @author xy
     */
    @Insert({"insert into ${tableName}(node_name,node_type,node_weight,parent_id) " +
            "values(#{node_name},${node_type},${node_weight},${parent_id});"
    })
    int insertIntoTable(@Param("tableName")String tableName,@Param("node_name")String name,@Param("node_type")int type,@Param("node_weight")double weight,@Param("parent_id")int id);

    /**
     * @author xy
     */
    List<IndexSymNode> getAllNodeInfo(String tableName);
}