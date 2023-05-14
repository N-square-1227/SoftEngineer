package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Sample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.ResultHandler;

import java.util.HashMap;
import java.util.List;

/**
 * by wxy
 * 试试springboot
 **/
@Mapper
public interface SampleMapper extends BaseMapper<Sample> {

    List<HashMap<String, Double>> getData();     // 我也不知道为啥要这样写，反正能获取

    List<String> getColName();

}
