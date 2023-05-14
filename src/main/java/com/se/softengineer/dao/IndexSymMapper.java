package com.se.softengineer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface IndexSymMapper extends BaseMapper<Indexsym> {
    @Update({"create table ${tableName}(" +
            "  `node_id` int NOT NULL AUTO_INCREMENT," +
            "  `node_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL," +
            "  `node_type` int NOT NULL," +
            "  `node_weight` double NOT NULL," +
            "  `parent_id` int NOT NULL," +
            "  PRIMARY KEY (`node_id`) USING BTREE" +
            ")" +
            "ENGINE = InnoDB AUTO_INCREMENT = 160 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;"
    })
    int createTable(@Param("tableName") String tableName);

    @Insert({"(insert into ${tableName}(node_name,node_type,node_weight,parent_id) " +
            "values(#{node_name},#{node_type},#{node_weight},#{parent_id});"})
    int insertIntoTable(@Param("tableName")String tableName,@Param("node_name")String name,@Param("node_type")int type,@Param("node_weight")double weight,@Param("parent_id")int id);
}
