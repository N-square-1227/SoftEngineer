package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.Sample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SampleService extends IService<Sample> {

    List<Sample> getData(String table_name);
    List<String> getColName(String table_name);
    boolean createDataTable(String tableName, List<String> columnList);
    boolean insertDataTable(@Param("tableName") String tableName, @Param("dataName") String dataName, List<String> dataList);
}