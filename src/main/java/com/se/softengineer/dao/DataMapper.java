package com.se.softengineer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DataMapper extends BaseMapper<data> {

    /**
     * 南希诺
     * 创建熵权法表
     *             "`node_id` int(11) NOT NULL AUTO_INCREMENT, " +
     * todo：修改前端的 tableName！
     * @param tableName 后端修改后的表名
     */
    @Update({"CREATE TABLE ${tableName} ( " +
            "`node_id` int(11) NOT NULL, " +
            "`node_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, " +
            "`node_type` int(11) NOT NULL, " +
            "`node_weight` double NOT NULL," +
            "`parent_id` int(11) NOT NULL," +
            " PRIMARY KEY (`node_id`) USING BTREE" +
            " ) ENGINE = InnoDB AUTO_INCREMENT = 160 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;"})
    void createTable(@Param("tableName") String tableName);

    /**
     * 南希诺
     * 删除表
     * @param tableName 后端修改后的表名
     */
    @Update({"drop table if exists ${tableName}"})
    void dropExistTable(@Param("tableName") String tableName);
}
