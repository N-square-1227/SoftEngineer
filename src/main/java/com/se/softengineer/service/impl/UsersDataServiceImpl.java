package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.entity.Users;
import com.se.softengineer.entity.UsersData;
import com.se.softengineer.mapper.IndexSymMapper;
import com.se.softengineer.mapper.SampleMapper;
import com.se.softengineer.mapper.UsersDataMapper;
import com.se.softengineer.service.UsersDataService;
import com.se.softengineer.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLSyntaxErrorException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UsersDataServiceImpl extends ServiceImpl<UsersDataMapper, UsersData> implements UsersDataService {

    @Resource
    UsersDataMapper usersDataMapper;

    @Resource
    IndexSymMapper indexSymMapper;


    // 已删除的数据20天后自动过期
    // 其实是用户登录的时候判断一遍是不是有过期的
    // 但是这样的话如果数据量太大可能吃不消（没关系）
    final int guaranteePeriod = 20;

    @Override
    public int createTable(String tableName) {
//        System.out.println("掉这里");
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

    public int delDeletedIndex(Users user){
        String tableName = user.getUserName() + "_deleted";
        List<UsersData> indexList = usersDataMapper.getDataList(tableName);
        for (UsersData index: indexList) {
            if(TimeUtil.timeDifference(index.getTime()) >= guaranteePeriod)
                usersDataMapper.deleteById(index.getId());
        }
        return 0;
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

    @Override
    public int addIndex(String tableName, String indexSymDTName) {
        List<String> indexTables = usersDataMapper.getIndexSymTableNames(tableName);
        if(indexTables.contains(indexSymDTName))    /* 如果已经包含应该删除原来的*/
            usersDataMapper.delIndex(tableName, indexSymDTName);

        Date date = new Date(System.currentTimeMillis());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String format = dateFormat.format(date);

        usersDataMapper.insertIntoTable(tableName, indexSymDTName + "_data", indexSymDTName, format);

        return 0;
    }

}
