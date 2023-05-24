package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.UsersData;
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
}