package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.entity.UsersData;
import com.se.softengineer.mapper.SampleMapper;
import com.se.softengineer.mapper.UsersDataMapper;
import com.se.softengineer.service.UsersDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UsersDataServiceImpl extends ServiceImpl<UsersDataMapper, UsersData> implements UsersDataService {

    @Resource
    UsersDataMapper usersDataMapper;

    @Override
    public int createTable(String tableName) {
        return usersDataMapper.createTable(tableName);
    }

    @Override
    public int insertIntoTable(String tableName, String dataTableName, String indexSymDTName) {
        return usersDataMapper.insertIntoTable(tableName, dataTableName, indexSymDTName);
    }

    @Override
    public int deleteTable(String table_name) {
        return usersDataMapper.deleteTable(table_name);
    }

    @Override
    public int renameTable(String origin_table, String new_table) {
        return usersDataMapper.renameTable(origin_table, new_table);
    }
}
