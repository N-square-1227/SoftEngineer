package com.se.softengineer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Users;
import com.se.softengineer.entity.data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface UsersMapper extends BaseMapper<Users> {
//    @Select("select X1,X2,X3,X4,X5,X6,X7,X8,X9,X10,X11,X12,X13,X14,X15,X16,X17,X18,X19,X20,X21,X22,X23 from se.data")
//    List<Map<String, Object>> getXColumns();
//
//    @Select("select * from se.data")
//    ArrayList<Double> getColumns();
}
