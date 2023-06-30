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

    /**
     * 已删除指标体系表中添加一条数据
     * @param tableName 数据表名
     * @param dataTableName 所属指标体系的数据的数据表名
     * @param indexSymDTName 指标体系表名
     * @param time 主要是删除表使用, 满20天永久删除
     **/
    @Insert({"insert into ${tableName}(dataTableName,indexSymDTName,time) " +
            "values(#{dataTableName},#{indexSymDTName},#{time});"
    })
    int insertIntoTable(@Param("tableName")String tableName,@Param("dataTableName")String dataTableName,
                        @Param("indexSymDTName")String indexSymDTName, @Param("time")String time);

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
