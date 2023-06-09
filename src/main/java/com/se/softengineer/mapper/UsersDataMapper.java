package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.UsersData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UsersDataMapper extends BaseMapper<UsersData> {

    int createTable(@Param("tableName") String tableName);

    @Insert({"insert into ${tableName}(dataTableName,indexSymDTName,uploadTime) " +
            "values(#{dataTableName},#{indexSymDTName},#{uploadTime});"
    })
    int insertIntoTable(@Param("tableName")String tableName,@Param("dataTableName")String dataTableName,
                        @Param("indexSymDTName")String indexSymDTName, @Param("uploadTime")String uploadTime);

    int deleteTable(String table_name);

    int delIndex(@Param("table_name") String userTable,@Param("indexName") String indexName);

    List<String> getIndexSymTableNames(String tableName);

    /**
     * 分页查询指标体系数据表
     **/
    IPage getISDTNamePage(IPage<UsersData> page,String tableName,@Param(Constants.WRAPPER)Wrapper wrapper);

    List<String> getDataTableNames(String tableName);

    int renameTable(String origin_name, String new_name);

}
