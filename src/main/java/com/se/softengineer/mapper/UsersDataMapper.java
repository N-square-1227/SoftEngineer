package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.UsersData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UsersDataMapper extends BaseMapper<UsersData> {
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

    int deleteTable(String table_name);

    List<String> getIndexSymTableNames(String tableName);

    List<String> getDataTableNames(String tableName);

    int renameTable(String origin_name, String new_name);

}
