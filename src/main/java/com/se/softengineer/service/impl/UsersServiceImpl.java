package com.se.softengineer.service.impl;

import com.se.softengineer.algorithm.Kmeans.*;
import com.se.softengineer.entity.Node;
import com.se.softengineer.entity.Sample;
import com.se.softengineer.mapper.UsersMapper;
import com.se.softengineer.entity.Users;
import com.se.softengineer.service.NodeService;
import com.se.softengineer.service.SampleService;
import com.se.softengineer.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 **/
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 增加一个用户的信息
     * @param users 用户实体
     * @return 插入成功与否
     */
    @Override
    public int addUsersInfo(Users users) {
        Users entity = new Users();
        entity.setUserEmail(users.getUserEmail());
        entity.setUserName(users.getUserName());
        entity.setUserPassword(users.getUserPassword());
        return usersMapper.insert(entity);
    }

}
