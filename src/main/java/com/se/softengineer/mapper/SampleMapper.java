package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.controller.ImportController;
import com.se.softengineer.entity.Sample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;

import static com.se.softengineer.controller.ImportController.createSql;

/**
 * by wxy
 * 映射数据的文件（一行一个Sample)
 **/
@Mapper
public interface SampleMapper extends BaseMapper<Sample> {

    @Value("#{ImportController.createSql}")
    String sql = createSql;

    List<HashMap<String, Double>> getData(String table_name);     // 我也不知道为啥要这样写，反正能获取

    List<String> getColName(String table_name);

    //mybatisplus不支持非常量注入
/*    @Update({createSql})
    int createTable(String tableName);*/
}