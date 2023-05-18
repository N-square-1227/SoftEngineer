package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.IndexSymNode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UsersDataMapper extends BaseMapper<IndexSymNode> {
    @Update({"CREATE TABLE ${tableName} (" +
            "  `id` int NOT NULL AUTO_INCREMENT," +
            "  `dataTableName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL," +
            "  `indexSymDTName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL," +
            "  PRIMARY KEY (`id`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"
    })
    int createTable(@Param("tableName") String tableName);

    @Insert({"insert into ${tableName}(dataTableName,indexSymDTName) " +
            "values(#{dataTableName},#{indexSymDTName});"
    })
    int insertIntoTable(@Param("tableName")String tableName,@Param("dataTableName")String type,@Param("indexSymDTName")String weight);


}
