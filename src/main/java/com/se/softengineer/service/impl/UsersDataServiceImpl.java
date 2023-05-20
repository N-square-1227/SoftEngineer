package com.se.softengineer.service.impl;

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
        return usersDataMapper.createTable(tableName);
    }

    @Override
    public int insertIntoTable(String tableName, String dataTableName, String indexSymDTName) {
        List<String> indexTables = usersDataMapper.getIndexSymTableNames(tableName);
        if(indexTables.contains(indexSymDTName))    /* 如果已经包含就不再写入新记录 */
            return 0;
        return usersDataMapper.insertIntoTable(tableName, dataTableName, indexSymDTName);
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
}
