package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.UsersData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface UsersDataService extends IService<UsersData> {

    int createTable(String tableName);

    int insertIntoTable(String tableName,String dataTableName,String indexSymDTName);


}
