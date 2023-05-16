package com.se.softengineer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.softengineer.entity.Userrole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserroleMapper extends BaseMapper<Userrole> {

    @Select("select roleName from role where roleID in(select roleID from userrole where userID=#{userID})")
    String queryRoleNameByUserID(int userID);

}
