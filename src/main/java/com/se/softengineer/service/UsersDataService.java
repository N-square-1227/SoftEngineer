package com.se.softengineer.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.UsersData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersDataService extends IService<UsersData> {

    int createTable(String tableName);

    int insertIntoTable(String tableName,String dataTableName,String indexSymDTName, String date);

    int deleteTable(String table_name);

    int delIndex(String userTable,String indexName);

    int addIndex(String tableName, String indexName);

    int renameTable(String origin_table, String new_table);


    /**
     * @author xx
     */
    List<String> getIndexSymTableNames(String tableName);

    IPage getISDTNamePage(IPage<UsersData> page, String tableName, Wrapper wrapper);
}
