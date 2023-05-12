package com.se.softengineer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper extends BaseMapper<data> {
}
