package com.se.softengineer.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.UsersData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UsersDataMapper extends BaseMapper<UsersData> {

    int createTable(@Param("tableName") String tableName);

    @Insert({"insert into ${tableName}(dataTableName,indexSymDTName) " +
            "values(#{dataTableName},#{indexSymDTName});"
    })
    int insertIntoTable(@Param("tableName")String tableName,@Param("dataTableName")String dataTableName,@Param("indexSymDTName")String indexSymDTName);


}
