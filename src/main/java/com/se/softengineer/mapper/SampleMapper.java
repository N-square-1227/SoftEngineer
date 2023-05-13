package com.se.softengineer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Sample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * by wxy
 * 试试springboot
 **/
@Mapper
public interface SampleMapper extends BaseMapper<Sample> {

    List<Sample>  getData();

}
