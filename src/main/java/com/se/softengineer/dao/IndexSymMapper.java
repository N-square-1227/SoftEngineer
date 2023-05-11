package com.se.softengineer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface IndexSymMapper extends BaseMapper<Indexsym> {

}
