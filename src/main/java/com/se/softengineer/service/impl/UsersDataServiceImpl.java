package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.entity.UsersData;
import com.se.softengineer.mapper.IndexSymMapper;
import com.se.softengineer.mapper.SampleMapper;
import com.se.softengineer.mapper.UsersDataMapper;
import com.se.softengineer.service.UsersDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

@Service
public class UsersDataServiceImpl extends ServiceImpl<UsersDataMapper, UsersData> implements UsersDataService {

    @Resource
    UsersDataMapper usersDataMapper;

    @Resource
    IndexSymMapper indexSymMapper;

    @Override
    public int createTable(String tableName) {
        System.out.println("掉这里");
        return usersDataMapper.createTable(tableName);
    }

    @Override
    public int insertIntoTable(String tableName, String dataTableName, String indexSymDTName, String date) {
        List<String> indexTables = usersDataMapper.getIndexSymTableNames(tableName);
        if(indexTables.contains(indexSymDTName))    /* 如果已经包含应该删除原来的*/
            usersDataMapper.delIndex(tableName, indexSymDTName);
        return usersDataMapper.insertIntoTable(tableName, dataTableName, indexSymDTName, date);
    }

    @Override
    public int deleteTable(String table_name) {
        try {
            List<String> indexSymTables = usersDataMapper.getIndexSymTableNames(table_name);
            List<String> dataTables = usersDataMapper.getDataTableNames(table_name);

            /* 删除用户名下的指标体系表 */
            for (String name : indexSymTables)
                indexSymMapper.dropExistTable(name);

            /* 删除用户名下的指标体系数据表 */
            for (String name : dataTables)
                indexSymMapper.dropExistTable(name);
        }catch (Exception e) {
            System.out.println("Table doesn't exists.");
        }

        return usersDataMapper.deleteTable(table_name);
    }

    @Override
    public int renameTable(String origin_table, String new_table) {
        return usersDataMapper.renameTable(origin_table, new_table);
    }

    @Override
    public List<String> getIndexSymTableNames(String tableName) {
        return usersDataMapper.getIndexSymTableNames(tableName);
    }

    @Override
    public IPage getISDTNamePage(IPage<UsersData> page, String tableName, Wrapper wrapper) {
        return usersDataMapper.getISDTNamePage(page, tableName, wrapper);
    }

    @Override
    public int delIndex(String userTable,String indexName){
        return usersDataMapper.delIndex(userTable,indexName);
    }
}
