package com.se.softengineer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.softengineer.mapper.UserroleMapper;
import com.se.softengineer.entity.Userrole;
import com.se.softengineer.service.UserroleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserroleServiceImpl extends ServiceImpl<UserroleMapper, Userrole> implements UserroleService {
    @Autowired
    UserroleMapper userroleMapper;
    @Override
    public String getRoleNameByUserID(int userID) {
        return userroleMapper.queryRoleNameByUserID(userID);
    }
}
